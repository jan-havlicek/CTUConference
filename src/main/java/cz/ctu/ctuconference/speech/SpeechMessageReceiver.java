/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.speech;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author Nick Nemame
 */
@Component
public class SpeechMessageReceiver implements MessageReceiver {

    @Autowired
    private UserRegistry registry;

    @Autowired
    private CallManager conversationManager;

	@Autowired
	MessageSender messageSender;

    @Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
            case "speech.speaking":
                broadcastSpeechInfo(jsonMessageData, session);
                break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "speech";
	}

	/**
     * Leaves conversation. Where there is only one participant left, it terminates
     * whole conversation and send info to last participant with "callTerminated" message
     * @param session
     * @throws IOException
     */
    private void broadcastSpeechInfo(JsonObject params, WebSocketSession session) throws IOException {
		final UserSession userSession = registry.getBySession(session);
		long conversationId = userSession.getUserCall().getConversationId();
        final Call call = conversationManager.getCall(conversationId);

		params.addProperty("participantId", userSession.getUserId());
		SocketMessageDTO message = new SocketMessageDTO("speech.speaking", params);

		for(UserSession _userSession : call.getParticipants()) {
			if(userSession == _userSession) continue;
			messageSender.sendCustomMessage(_userSession, message);
		}
    }

}
