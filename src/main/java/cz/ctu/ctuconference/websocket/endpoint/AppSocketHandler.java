package cz.ctu.ctuconference.websocket.endpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cz.ctu.ctuconference.authentication.AuthenticationMessageReceiver;
import cz.ctu.ctuconference.call.CallMessageReceiver;
import cz.ctu.ctuconference.contact.ContactMessageReceiver;
import cz.ctu.ctuconference.conversation.ConversationMessageReceiver;
import cz.ctu.ctuconference.friendship.FriendshipMessageReceiver;
import cz.ctu.ctuconference.group.GroupMessageReceiver;
import cz.ctu.ctuconference.handouts.HandoutsMessageReceiver;
import cz.ctu.ctuconference.notification.NotificationMessageReceiver;
import cz.ctu.ctuconference.speech.SpeechMessageReceiver;
import cz.ctu.ctuconference.stats.StatsMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import cz.ctu.ctuconference.utils.communication.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick nemame on 11.12.2016.
 */
@Component
public class AppSocketHandler extends TextWebSocketHandler {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private CallMessageReceiver callMessageReceiver;

	@Autowired
	private ContactMessageReceiver contactMessageReceiver;

	@Autowired
	private ConversationMessageReceiver conversationMessageReceiver;

	@Autowired
	private AuthenticationMessageReceiver authenticationMessageReceiver;

	@Autowired
	private SpeechMessageReceiver speechMessageReceiver;

	@Autowired
	private HandoutsMessageReceiver handoutsMessageReceiver;

	@Autowired
	private GroupMessageReceiver groupMessageReceiver;

	@Autowired
	private FriendshipMessageReceiver friendshipMessageReceiver;

	@Autowired
	private NotificationMessageReceiver notificationMessageReceiver;

	@Autowired
	private StatsMessageReceiver statsMessageReceiver;

	private List<MessageReceiver> messageReceiverList;

	@PostConstruct
	public void populateReceiverList() {
		this.messageReceiverList = new ArrayList<>();
		messageReceiverList.add(callMessageReceiver);
		messageReceiverList.add(contactMessageReceiver);
		messageReceiverList.add(conversationMessageReceiver);
		messageReceiverList.add(authenticationMessageReceiver);
		messageReceiverList.add(speechMessageReceiver);
		messageReceiverList.add(handoutsMessageReceiver);
		messageReceiverList.add(groupMessageReceiver);
		messageReceiverList.add(callMessageReceiver);
		messageReceiverList.add(friendshipMessageReceiver);
		messageReceiverList.add(notificationMessageReceiver);
		messageReceiverList.add(statsMessageReceiver);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("Close session: "+session.getId());
		callMessageReceiver.handleConnectionClose(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
		String message = jsonTextMessage.getPayload();
		try {
			JsonParser parser = new JsonParser();
			JsonObject jsonMessage = parser.parse(message).getAsJsonObject();
			//logIncomingMessage(jsonMessage, session);
			if(jsonMessage.get("type") == null) throw new WSMessageException(WSMessageException.TYPE_MISSING);
			if(jsonMessage.get("data") == null) throw new WSMessageException(WSMessageException.DATA_MISSING);
			String messageType = jsonMessage.get("type").getAsString();
			JsonObject jsonMessageData = jsonMessage.getAsJsonObject("data");
			String[] messageTypeParts = messageType.split("\\.");
			messageReceiverList
					.stream()
					.filter(receiver -> messageTypeParts[0].equals(receiver.getListenedMessagePrefix()))
					.findFirst()
					.orElseThrow(() -> new WSMessageException(WSMessageException.UNKNOWN_TYPE))
					.handleMessage(messageType, jsonMessageData, session);
		} catch(WSMessageException e) {
			messageSender.sendErrorMessage(session, e.getMessage(), message);
		} catch (Exception e) {
			messageSender.sendErrorMessage(session, "Processing of incoming message failed. Message: "+e.getMessage(), message);
		}
	}
}
