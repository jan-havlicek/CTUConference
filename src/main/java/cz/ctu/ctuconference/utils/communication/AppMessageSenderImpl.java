package cz.ctu.ctuconference.utils.communication;

import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.contact.service.ContactService;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.service.GroupService;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 23.12.2016.
 */
@Service
public class AppMessageSenderImpl implements AppMessageSender {

	@Autowired
	private MessageSender sender;

	@Autowired
	private UserRegistry userRegistry;

	@Autowired
	private CallManager callManager;

	@Autowired
	private ContactService contactService;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private GroupService groupService;

	@Override
	public void sendAnonymous(WebSocketSession session, String messageType, Object messageData) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		sender.sendAnonymousMessage(session, message);
	}

	@Override
	public void send(long userId, String messageType, Object messageData) throws IOException {
		UserSession userSession = userRegistry.getByUserId(userId);
		if(userSession != null) {
			send(userSession, messageType, messageData);
		}
	}

	@Override
	public void send(UserSession userSession, String messageType, Object messageData) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		sender.sendCustomMessage(userSession, message);
	}

	@Override
	public void sendToConversationMembers(long conversationId, long senderId, String messageType, Object messageData) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(Long memberId : getConversationMemberIdList(conversationId)) {
			UserSession receiverSession = userRegistry.getByUserId(memberId);
			if(receiverSession == null || receiverSession.getUserId() == senderId) continue;
			sender.sendCustomMessage(receiverSession, message);
		}
	}

	@Override
	public void sendToCallParticipants(long conversationId, long senderId, String messageType, Object messageData) throws IOException {
		sendToPendingParticipants(conversationId, senderId, messageType, messageData, null);
		sendToCallingParticipants(conversationId, senderId, messageType, messageData, null);
	}

	@Override
	public void sendToAvailableMembers(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for (long memberId : getConversationMemberIdList(conversationId)) {
			if(callManager.isUserBusy(memberId)) continue;
			if (memberId == senderId) continue;
			UserSession receiver = userRegistry.getByUserId(memberId);
			if(receiver == null) continue;
			sender.sendCustomMessage(receiver, message);
			if(userSentCallback != null) userSentCallback.onEvent(receiver);
		}
	}

	@Override
	public void sendToCallingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(UserSession receiver : callManager.getCall(conversationId).getParticipants()) {
			if(receiver.getUserId() == senderId) continue;
			sender.sendCustomMessage(receiver, message);
			if(userSentCallback != null) userSentCallback.onEvent(receiver);
		}
	}

	@Override
	public void sendToTransmittingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(UserSession receiver : callManager.getCall(conversationId).getTransmittingParticipants()) {
			if(receiver.getUserId() == senderId) continue;
			sender.sendCustomMessage(receiver, message);
			if(userSentCallback != null) userSentCallback.onEvent(receiver);
		}
	}

	@Override
	public void sendToListeningParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(UserSession receiver : callManager.getCall(conversationId).getListeningParticipants()) {
			if(receiver.getUserId() == senderId) continue;
			sender.sendCustomMessage(receiver, message);
			if(userSentCallback != null) userSentCallback.onEvent(receiver);
		}
	}

	@Override
	public void sendToPendingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(UserSession receiver : callManager.getCall(conversationId).getPendingParticipants()) {
			if(receiver.getUserId() == senderId) continue;
			sender.sendCustomMessage(receiver, message);
			if(userSentCallback != null) userSentCallback.onEvent(receiver);
		}
	}

	@Override
	public void sendToFriends(long userId, String messageType, Object messageData) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(Friendship friendship : contactService.getFriendshipList(userId)) {
			if(friendship.getState() != ContactAuthState.ACCEPTED) continue;
			UserSession userSession = userRegistry.getByUserId(friendship.getFriend().getId());
			if(userSession == null) continue;
			sender.sendCustomMessage(userSession, message);
		}
	}

	@Override
	public void sendToMyGroupsMembers(long userId, String messageType, Object messageData, GroupMessageModifier modifier) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(Group group : contactService.getGroupList(userId)) {
			modifier.modifyGroupMessage(group, messageData);
			for(AppUser member : groupService.getGroupMembers(group.getId())) {
				if(member.getId() == userId) continue;
				UserSession userSession = userRegistry.getByUserId(member.getId());
				if(userSession == null) continue;
				sender.sendCustomMessage(userSession, message);
			}
		}
	}

	@Override
	public void sendToGroupMembers(long groupId, String messageType, Object messageData) throws IOException {
		SocketMessageDTO message = new SocketMessageDTO(messageType, messageData);
		for(AppUser member : groupService.getGroupMembers(groupId)) {
			UserSession userSession = userRegistry.getByUserId(member.getId());
			if(userSession == null) continue;
			sender.sendCustomMessage(userSession, message);
		}
	}

	/**
	 * Returns id list of conversation members. If it has active call, it contains member list loaded in call object,
	 * otherwise it is necessary to load it from database.
	 * @param conversationId
	 * @return
	 */
	private List<Long> getConversationMemberIdList(long conversationId) {
		Call call = callManager.getCall(conversationId);
		List<Long> memberIdList = call == null ? conversationService.getParticipantIdList(conversationId) : call.getMembers();
		return memberIdList;
	}
}
