/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.utils.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.domain.ParticipantType;
import cz.ctu.ctuconference.contact.domain.ContactAction;
import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MemberPrivilege;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.notification.domain.RelationshipAction;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.json.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author Nick Nemame
 */
@Component
public class MessageSenderImpl implements MessageSender {

	private Gson gson;

	public MessageSenderImpl() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ContactAction.class, new ContactActionTypeSerializer() );
		gsonBuilder.registerTypeAdapter(ContactState.class, new ContactStateTypeSerializer() );
		gsonBuilder.registerTypeAdapter(ContactAuthState.class, new ContactAuthStateTypeSerializer() );
		gsonBuilder.registerTypeAdapter(CallState.class, new CallStateTypeSerializer() );
		gsonBuilder.registerTypeAdapter(CallType.class, new CallTypeTypeSerializer() );
		gsonBuilder.registerTypeAdapter(GroupType.class, new GroupTypeTypeSerializer() );
		gsonBuilder.registerTypeAdapter(MemberPrivilege.class, new MemberPrivilegeTypeSerializer() );
		gsonBuilder.registerTypeAdapter(MembershipRole.class, new MembershipRoleTypeSerializer() );
		gsonBuilder.registerTypeAdapter(RelationshipAction.class, new RelationshipActionTypeSerializer() );
		gsonBuilder.registerTypeAdapter(ParticipantType.class, new ParticipantTypeTypeSerializer() );

		//http://stackoverflow.com/questions/19588020/gson-serialize-a-list-of-polymorphic-objects
//		RuntimeTypeAdapterFactory<ConversationEntityDTO> adapter =
//				RuntimeTypeAdapterFactory
//						.of(ConversationEntityDTO.class)
//						.registerSubtype(GroupDTO.class)
//						.registerSubtype(FriendDTO.class);
//		gsonBuilder.registerTypeAdapterFactory(adapter);

		gson = gsonBuilder.create();
	}

	public void sendErrorMessage(WebSocketSession wsSession, String errorMessage, String incomingMessage) throws IOException {
		JsonObject jsonMessage = new JsonObject();
		jsonMessage.addProperty("type", "message.error");
		String message = "Error: "+errorMessage+", your message: "+incomingMessage;
		jsonMessage.addProperty("message", message);
		synchronized (wsSession) {
			TextMessage msg = new TextMessage(jsonMessage.toString().getBytes("UTF-8"));
			wsSession.sendMessage(msg);
			//wsSession.getBasicRemote().sendText(jsonMessage.toString());
		}
	}

	public void sendCustomMessage(UserSession userSession, SocketMessageDTO message) throws IOException {
		Object outputData = message.getData();
		JsonObject jsonMessage = new JsonObject();
		jsonMessage.addProperty("type", message.getType());
		if(outputData != null) {
			if(outputData instanceof JsonObject) {
				jsonMessage.add("data", (JsonObject) outputData);
			} else {
				JsonElement data = gson.toJsonTree(outputData);
				jsonMessage.add("data", data);
			}
		}
		sendMessage(userSession, jsonMessage);
	}

	public void sendMessage(UserSession userSession, JsonObject message) throws IOException {
		if(!userSession.getSession().isOpen()) return;
		synchronized (userSession.getSession()) {
			TextMessage msg = new TextMessage(message.toString().getBytes("UTF-8"));
			userSession.getSession().sendMessage(msg);
			//userSession.getSession().getBasicRemote().sendText(message.toString());
		}
	}

	public void sendAnonymousMessage(WebSocketSession wsSession, SocketMessageDTO message) throws IOException {
		Object outputData = message.getData();
		JsonObject jsonMessage = new JsonObject();
		jsonMessage.addProperty("type", message.getType());
		if(outputData != null) {
			JsonElement data = gson.toJsonTree(outputData);
			jsonMessage.add("data", data);
		}

		synchronized (wsSession) {
			TextMessage msg = new TextMessage(jsonMessage.toString().getBytes("UTF-8"));
			wsSession.sendMessage(msg);
			//wsSession.getBasicRemote().sendText(jsonMessage.toString());
		}
	}
}
