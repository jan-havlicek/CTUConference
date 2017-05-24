/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.conversation.service.comm.ConversationCommService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Nick Nemame
 */
@Component
public class ConversationMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	ConversationCommService conversationCommService;

    @Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
            case "conversation.messages":
				getConversationMessages(jsonMessageData, session);
				break;
            case "conversation.new-message": //initiator cancel call, server broadcast callTerminated message
                handleIncomingMessage(jsonMessageData, session);
                break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "conversation";
	}

	/**
	 * Returns all messages for particular conversation
	 * @param params
	 * @param session
	 */
	private void getConversationMessages(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		long conversationId = parser.getLongParameter(params, "conversationId");
		UserSession userSession = getUserSession(session);
		authorizeConversationAccess(conversationId, session);
		conversationCommService.getConversationMessages(userSession, conversationId);
	}

	/**
	 *
	 * @param params
	 * @param session
	 */
	private void handleIncomingMessage(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long conversationId = parser.getLongParameter(params, "conversationId");
		final String message = parser.getStringParameter(params, "message");
		UserSession userSession = getUserSession(session);
		MessageDTO messageDTO = new MessageDTO(null, message, new Date(), false, userSession.getUserId(), userSession.getUserName(), "incoming");
		conversationCommService.handleIncomingMessage(userSession, conversationId, messageDTO);
	}
}
