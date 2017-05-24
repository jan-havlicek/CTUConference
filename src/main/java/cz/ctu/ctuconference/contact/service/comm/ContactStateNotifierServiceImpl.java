package cz.ctu.ctuconference.contact.service.comm;

import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;
import cz.ctu.ctuconference.contact.domain.ContactAction;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.contact.dto.ContactListContainerGroup;
import cz.ctu.ctuconference.contact.dto.ContactListItemPrivate;
import cz.ctu.ctuconference.contact.dto.state.ConversationNotificationDTO;
import cz.ctu.ctuconference.contact.dto.state.FriendNotificationDTO;
import cz.ctu.ctuconference.contact.dto.state.GroupMemberNotificationDTO;
import cz.ctu.ctuconference.contact.dto.state.GroupNotificationDTO;
import cz.ctu.ctuconference.conversation.dto.ParticipantDTO;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import cz.ctu.ctuconference.utils.communication.GroupMessageModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * Created by Nick nemame on 20.11.2016.
 */
@Service
public class ContactStateNotifierServiceImpl implements ContactStateNotifierService {

	@Autowired
	private AppMessageSender messageSender;

	@Override
	public void notifyUserStateChanged(long userId, ContactState state) throws IOException {
		notifyFriendStateChanged(userId, state);
		notifyGroupMemberStateChanged(userId, state);
	}

	@Override
	public void notifyUserInCall(long userId, long conversationId) throws IOException {
		notifyFriendStateChanged(userId, ContactState.DO_NOT_DISTURB);
		notifyGroupMemberInCall(userId, conversationId);
	}

	@Override
	public void notifyUserStopCalling(long userId) throws IOException {
		notifyUserStateChanged(userId, ContactState.ONLINE);
	}

	private void notifyFriendStateChanged(long friendId, ContactState state) throws IOException {
		FriendNotificationDTO notification = new FriendNotificationDTO(
				friendId,
				ContactAction.STATE_CHANGED,
				state
		);
		messageSender.sendToFriends(friendId, "contact.friend-state", notification);
	}

	@Override
	public void notifyFriendAdded(ContactListItemPrivate friendItem) throws IOException {
		FriendNotificationDTO notification = new FriendNotificationDTO(friendItem);
		long userId = friendItem.getFriend().getId();
		messageSender.sendToFriends(userId, "contact.friend-state", notification);
	}

	@Override
	public void notifyFriendRemoved(long friendId) throws IOException {
		FriendNotificationDTO notification = new FriendNotificationDTO(
				friendId,
				ContactAction.REMOVED,
				null
		);
		messageSender.sendToFriends(friendId, "contact.friend-state", notification);
	}

	private void notifyGroupMemberStateChanged(long memberId, ContactState state) throws IOException {
		GroupMemberNotificationDTO notification = new GroupMemberNotificationDTO(
				memberId,
				ContactAction.STATE_CHANGED,
				state
		);
		notifyAllGroupMemberState(memberId, notification, -1);
	}

	private void notifyGroupMemberInCall(long memberId, long conversationId) throws IOException {
		GroupMemberNotificationDTO notification = new GroupMemberNotificationDTO(
				memberId,
				ContactAction.STATE_CHANGED,
				ContactState.DO_NOT_DISTURB
		);
		notifyAllGroupMemberState(memberId, notification, conversationId);
	}

	@Override
	public void notifyGroupMemberAdded(ParticipantDTO member, long conversationId) throws IOException {
		GroupMemberNotificationDTO notification = new GroupMemberNotificationDTO(member, conversationId);
		notifyGroupMemberState(member.getId(), notification, conversationId);
	}

	@Override
	public void notifyGroupMemberRemoved(long memberId, long conversationId) throws IOException {
		GroupMemberNotificationDTO notification = new GroupMemberNotificationDTO(
				memberId,
				ContactAction.REMOVED,
				null
		);
		notification.setConversationId(conversationId);
		notifyGroupMemberState(memberId, notification, conversationId);
	}

	private void notifyGroupMemberState(long userId, GroupMemberNotificationDTO notification, long conversationId) throws IOException {
		messageSender.sendToConversationMembers(conversationId, userId, "contact.group-member-state", notification);
	}

	private void notifyAllGroupMemberState(long userId, GroupMemberNotificationDTO notification, long conversationId) throws IOException {
		boolean isInCall = notification.getState() == ContactState.IN_CALL;

		GroupMessageModifier modifier = (group, messageData) -> {
			long groupConversationId = group.getConversation().getId();
			if(isInCall) {
				ContactState groupContactState = groupConversationId == conversationId ?
						ContactState.IN_CALL : ContactState.DO_NOT_DISTURB;
				((GroupMemberNotificationDTO) messageData).setState(groupContactState);
			}
			((GroupMemberNotificationDTO) messageData).setGroupId(group.getId());
		};

		messageSender.sendToMyGroupsMembers(userId, "contact.group-member-state", notification, modifier);

		//if(isInCall) notification.setState(ContactState.IN_CALL);
	}

	@Override
	public void notifyGroupRemoved(long groupId) throws IOException {
		GroupNotificationDTO notification = new GroupNotificationDTO(
				groupId,
				ContactAction.REMOVED
		);
		notifyGroupState(groupId, notification);
	}

	@Override
	public void notifyGroupAdded(ContactListContainerGroup groupContainer, long receiverId) throws IOException {
		GroupNotificationDTO notification = new GroupNotificationDTO(groupContainer);
		long groupId = groupContainer.getGroup().getId();
		messageSender.send(receiverId, "contact.group-state", notification);
	}


	@Override
	public void notifyGroupStateChanged(long groupId) throws IOException {
		throw new NotImplementedException();
	}

	private void notifyGroupState(long groupId, GroupNotificationDTO notification) throws IOException {
		messageSender.sendToGroupMembers(groupId, "contact.group-state", notification);
	}

	@Override
	public void notifyGroupCallState(long conversationId, CallState callState, CallType callType, CallInfoDTO callInfo) throws IOException {
		ConversationNotificationDTO notification = new ConversationNotificationDTO(conversationId, callState, callType, callInfo);
		messageSender.sendToConversationMembers(conversationId, -1, "contact.conversation-state", notification);
	}
}
