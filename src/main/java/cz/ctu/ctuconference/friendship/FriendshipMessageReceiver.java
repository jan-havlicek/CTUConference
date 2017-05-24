/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.friendship;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.friendship.service.comm.FriendshipCommService;
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
public class FriendshipMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	FriendshipCommService friendshipCommService;

    @Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
			case "friendship.suggest":
				suggestFriendships(jsonMessageData, session);
				break;
			case "friendship.request":
				requestFriendship(jsonMessageData, session);
				break;
			case "friendship.accept":
                acceptFriendship(jsonMessageData, session);
                break;
			case "friendship.reject":
				rejectFriendship(jsonMessageData, session);
				break;
			case "friendship.leave":
				leaveFriendship(jsonMessageData, session);
				break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "friendship";
	}

	private void suggestFriendships(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
		friendshipCommService.suggestFriendship(userSession);
	}

	/**
	 * Returns all messages for particular conversation
	 * @param params
	 * @param session
	 */
	private void requestFriendship(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long requestedUserId = parser.getLongParameter(params, "userId");
		UserSession userSession = getUserSession(session);
		friendshipCommService.requestFriendship(userSession, requestedUserId);
	}

	private void acceptFriendship(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long acceptedUserId = parser.getLongParameter(params, "userId");
		UserSession userSession = getUserSession(session);
		friendshipCommService.acceptFriendship(userSession, acceptedUserId);
	}

	private void rejectFriendship(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long rejectedUserId = parser.getLongParameter(params, "userId");
		UserSession userSession = getUserSession(session);
		friendshipCommService.rejectFriendship(userSession, rejectedUserId);
	}

	private void leaveFriendship(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long userId = parser.getLongParameter(params, "userId");
		UserSession userSession = getUserSession(session);
		friendshipCommService.leaveFriendship(userSession, userId);
	}
}
