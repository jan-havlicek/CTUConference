package cz.ctu.ctuconference.notification;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.notification.service.comm.NotificationCommService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Component
public class NotificationMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	NotificationCommService notificationCommService;

	@Override
	public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws WSMessageException, IOException, Exception {
		switch(messageType) {
			case "notification.list":
				notificationList(jsonMessageData, session);
				break;
			case "notification.mark-read":
				markRead(jsonMessageData, session);
				break;
			case "notification.mark-unread":
				markUnread(jsonMessageData, session);
				break;
		}
	}

	@Override
	public String getListenedMessagePrefix() {
		return "notification";
	}


	private void notificationList(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
		notificationCommService.listNotifications(userSession);
	}

	private void markRead(JsonObject params, WebSocketSession session) throws WSMessageException {
		UserSession userSession = getUserSession(session);
		long notificationId = parser.getLongParameter(params, "notificationId");
		notificationCommService.markRead(userSession, notificationId);

	}

	private void markUnread(JsonObject params, WebSocketSession session) throws WSMessageException {
		UserSession userSession = getUserSession(session);
		long notificationId = parser.getLongParameter(params, "notificationId");
		notificationCommService.markUnread(userSession, notificationId);
	}

}
