/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.call;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.call.domain.ParticipantType;
import cz.ctu.ctuconference.call.service.comm.CallCommService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author Nick Nemame
 */
@Component
public class CallMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	CallCommService callCommService;

	@Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
            case "call.invoke": //client invoked call, server broadcast call request
                invokeCall(jsonMessageData, session);
                break;
            case "call.cancel": //initiator cancel call, server broadcast callTerminated message
                cancelCall(jsonMessageData, session);
                break;
            case "call.accept": //call join conversation
                joinCall(jsonMessageData, session);
                break;
            case "call.reject":
                rejectCall(jsonMessageData, session);
                break;
            case "call.receiveVideoFrom":
                receiveVideoFrom(jsonMessageData, session);
                break;
            case "call.leave": //user leaves conversation. If conversation is to be terminated "callTerminated" message is sent.
                leaveCall(session);
                break;
            case "call.onIceCandidate":
                handleIceCandidate(jsonMessageData, session);
				break;
			case "call.modifyParticipant":
				modifyParticipant(jsonMessageData, session);
				break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "call";
	}

	/**
	 * Handle the close of user session. It removes the user from call, if the user participated the call, and notify
	 * his related contacts about their state.
	 * @param session
	 * @throws IOException
	 */
	public void handleConnectionClose(WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
        if(userSession != null) {
			callCommService.handleUserSessionClose(userSession);
        }
    }

    /**
     * Server broadcast "callRequest" message to all other callees
     * @param params
     * @param session
     * @throws IOException
	 * @throws WSMessageException
     */
    private void invokeCall(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		final long conversationId = parser.getLongParameter(params, "conversationId");
		final String callType =  parser.getStringParameter(params, "callType");
		UserSession initiatorSession = getUserSession(session);
		authorizeConversationAccess(conversationId, session);
		callCommService.invokeCall(initiatorSession, conversationId, CallType.getByName(callType));
    }

    /**
     * Join the user into the conversation call. If this is the first user joined,
	 * it is necessary to join the caller too (they are still waiting for accept)
     * @param params
     * @param session
     * @throws IOException
	 * @throws WSMessageException
     */
    private void joinCall(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
        final long conversationId = parser.getLongParameter(params, "conversationId");
        UserSession userSession = getUserSession(session);
        authorizeConversationAccess(conversationId, session);
		callCommService.joinCall(conversationId, userSession);
    }

    /**
     * This method is called by initiator when cancel button clicked. It broadcast
     * "cancelCall" message to all pending memberList.
     * @param params
     * @param session
     * @throws IOException
	 * @throws WSMessageException
     */
    private void cancelCall(JsonObject params, WebSocketSession session) throws IOException, WSMessageException, Exception {
        //final long conversationId = parser.getLongParameter(params, "conversationId");
        UserSession userSession = getUserSession(session);
		authorizeCallInitiatorAccess(session);
		callCommService.cancelCall(userSession);
    }

    /**
     * This method is called by callee when the reject button is clicked.
     * It removes itself from list of pending memberList.
     * If there are not any other pending memberList (thus initiator is only person waiting
     * for start of communication), "callTerminated" message is sent to him.
     * @param params
     * @param session
     * @throws IOException
	 * @throws WSMessageException
     */
    private void rejectCall(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
        long conversationId = parser.getLongParameter(params, "conversationId");
        UserSession userSession = getUserSession(session);
		authorizeConversationAccess(conversationId, session);
		callCommService.rejectCall(conversationId, userSession);
    }

	/**
	 *
	 * @param params
	 * @param session
	 * @throws IOException
	 * @throws WSMessageException
	 */
    private void receiveVideoFrom(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
        long senderId = parser.getLongParameter(params, "senderId");
        String sdpOffer = parser.getStringParameter(params, "sdpOffer");
		UserSession userSession = getUserSession(session);
		callCommService.receiveVideoFrom(userSession, senderId, sdpOffer);

    }

    /**
     * Leaves call. Where there is only one participant left, it terminates
     * whole call and send info to last participant and possible pending participants
	 * with "callTerminated" message
     * @param session
     * @throws IOException
     */
    private void leaveCall(WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
		callCommService.leaveCall(userSession);
    }

	/**
	 * Receive ICE candidate for participant and add to UserCall
	 * @param params
	 * @param session
	 * @throws WSMessageException
	 */
    private void handleIceCandidate(JsonObject params, WebSocketSession session) throws WSMessageException {
		final UserSession userSession = getUserSession(session);
		JsonObject candidateObj = parser.getJsonObjectParameter(params, "candidate");
		long participantId = parser.getLongParameter(params, "participantId");
		IceCandidate candidate = new IceCandidate(candidateObj.get("candidate").getAsString(),
				candidateObj.get("sdpMid").getAsString(), candidateObj.get("sdpMLineIndex").getAsInt());
		callCommService.handleIceCandidate(userSession, participantId, candidate);
	}

	/**
	 * Modify call participant info - its role in the call and transmission info
	 * @param params
	 * @param session
	 * @throws WSMessageException
	 */
	public void modifyParticipant(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		authorizeCallAccessAs(session, ParticipantType.LECTOR);
		UserSession lectorSession = getUserSession(session);
		long userId = parser.getLongParameter(params, "id");
		boolean videoMuted = parser.getBooleanParameter(params, "videoMuted");
		boolean audioMuted = parser.getBooleanParameter(params, "audioMuted");
		boolean isInitiator = parser.getBooleanParameter(params, "isInitiator");
		ParticipantType type = ParticipantType.fromNumber(parser.getLongParameter(params, "type"));
		ParticipantInfo participantInfo = new ParticipantInfo(userId, audioMuted, videoMuted, isInitiator, type);
		callCommService.modifyParticipant(lectorSession, participantInfo);
	}
}
