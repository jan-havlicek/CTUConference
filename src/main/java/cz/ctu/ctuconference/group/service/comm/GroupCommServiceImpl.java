package cz.ctu.ctuconference.group.service.comm;

import cz.ctu.ctuconference.contact.dto.SuggestionsDTO;
import cz.ctu.ctuconference.contact.service.ContactListService;
import cz.ctu.ctuconference.contact.dto.ContactListContainerGroup;
import cz.ctu.ctuconference.contact.service.ContactSuggestionService;
import cz.ctu.ctuconference.contact.service.comm.ContactStateNotifierService;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.conversation.dto.ParticipantDTO;
import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.dto.GroupDTO;
import cz.ctu.ctuconference.group.dto.MembershipDTO;
import cz.ctu.ctuconference.group.service.GroupService;
import cz.ctu.ctuconference.notification.domain.GroupMembershipNotification;
import cz.ctu.ctuconference.notification.domain.RelationshipAction;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import cz.ctu.ctuconference.notification.service.NotificationConverter;
import cz.ctu.ctuconference.notification.service.NotificationService;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Service
public class GroupCommServiceImpl implements GroupCommService {

	@Autowired
	AppMessageSender messageSender;

	@Autowired
	GroupService groupService;

	@Autowired
	ContactListService contactListService;

	@Autowired
	ContactSuggestionService contactSuggestionService;

	@Autowired
	ContactStateNotifierService contactNotifierService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	NotificationConverter notificationConverter;

	@Override
	public void getMemberships(UserSession userSession, long groupId) throws IOException {
		List<MembershipDTO> memberships = groupService.getGroupMemberships(groupId);
		messageSender.send(userSession, "group.memberships", memberships);
	}

	@Override
	public void createGroup(UserSession creatorSession, String name, GroupType type) throws IOException {
		long creatorId = creatorSession.getUserId();
		long groupId = groupService.createGroup(name, type, creatorId);
		ContactListContainerGroup contactListGroup = contactListService.getGroupContainer(groupId, creatorId);
		contactNotifierService.notifyGroupAdded(contactListGroup, creatorId);
		messageSender.send(creatorSession, "group.create", new GroupDTO(groupId, name, type, null));
	}

	@Override
	public void removeGroup(UserSession userSession, long groupId) throws IOException {
		groupService.removeGroup(groupId);
		messageSender.send(userSession, "group.remove", new GroupDTO(groupId, null, null, null));
		contactNotifierService.notifyGroupRemoved(groupId);
	}

	@Override
	public void suggestGroups(UserSession userSession) throws IOException {
		long userId = userSession.getUserId();
		List<GroupDTO> suggestions;
		suggestions = contactSuggestionService.getGroupSuggestions(userId);
		messageSender.send(userSession, "group.suggest", new SuggestionsDTO<>("group", suggestions));
	}

	@Override
	public void requestMembership(UserSession userSession, long groupId) throws IOException {
		GroupMembership membership = groupService.requestMembership(userSession.getUserId(), groupId);
		List<AppUser> groupAdmins = groupService.getAdminMembers(groupId);
		for(AppUser admin: groupAdmins) {
			GroupMembershipNotification notification = new GroupMembershipNotification(
					admin, membership, RelationshipAction.REQUESTED);
			notificationService.addNotification(notification);
			NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
			messageSender.send(admin.getId(), "notification.group", notificationDTO);
		}
	}

	@Override
	public void acceptMembership(UserSession adminSession, long userId, long groupId, MembershipRole role) throws IOException {
		GroupMembership membership = groupService.acceptMembership(userId, groupId, role);
		GroupMembershipNotification notification = new GroupMembershipNotification(
				membership.getUser(), membership, RelationshipAction.ACCEPTED);
		notificationService.addNotification(notification);
		//send contact list update to user
		ContactListContainerGroup contactListGroup = contactListService.getGroupContainer(groupId, userId);
		contactNotifierService.notifyGroupAdded(contactListGroup, userId);
		//send new member notification to all group members
		AppUser user = membership.getUser();
		ParticipantDTO participant = new ParticipantDTO(user.getId(), user.getFirstName(), user.getLastName());
		contactNotifierService.notifyGroupMemberAdded(participant, getMembershipConversationId(membership));
		//send notification about the group membership to the user
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(userId, "notification.group", notificationDTO);
	}

	@Override
	public void rejectMembership(UserSession adminSession, long userId, long groupId) throws IOException {
		GroupMembership membership = groupService.rejectMembership(userId, groupId);
		GroupMembershipNotification notification = new GroupMembershipNotification(
				membership.getUser(), membership, RelationshipAction.REJECTED);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(userId, "notification.group", notificationDTO);
	}

	@Override
	public void removeMembership(UserSession adminSession, long userId, long groupId) throws IOException {
		GroupMembership membership = groupService.removeMembership(userId, groupId);
		GroupMembershipNotification notification = new GroupMembershipNotification(
				membership.getUser(), membership, RelationshipAction.REMOVED);
		notificationService.addNotification(notification);
		//send contact list update
		//contactNotifierService.notifyGroupMemberRemoved(userIdf);
		//send notification about group membership removal
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(userId, "notification.group", notificationDTO);
	}

	@Override
	public void changeRole(UserSession adminSession, long userId, long groupId, MembershipRole role) throws IOException {
		GroupMembership membership = groupService.changeRole(userId, groupId, role);
		GroupMembershipNotification notification = new GroupMembershipNotification(
				membership.getUser(), membership, RelationshipAction.CHANGED_PERMISSIONS);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(userId, "notification.group", notificationDTO);
	}

	@Override
	public void leaveGroup(UserSession userSession, long groupId) throws IOException {
		GroupMembership membership = groupService.removeMembership(userSession.getUserId(), groupId);
		long conversationId = getMembershipConversationId(membership);
		contactNotifierService.notifyGroupMemberRemoved(userSession.getUserId(), conversationId);
	}

	private long getMembershipConversationId(GroupMembership membership) {
		return membership.getGroup().getConversation().getId();
	}
}
