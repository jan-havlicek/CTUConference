package cz.ctu.ctuconference.friendship.service.comm;

import cz.ctu.ctuconference.contact.dto.ContactListItem;
import cz.ctu.ctuconference.contact.dto.ContactListItemPrivate;
import cz.ctu.ctuconference.contact.dto.SuggestionsDTO;
import cz.ctu.ctuconference.contact.service.ContactListService;
import cz.ctu.ctuconference.contact.service.ContactSuggestionService;
import cz.ctu.ctuconference.contact.service.comm.ContactStateNotifierService;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.friendship.dto.FriendDTO;
import cz.ctu.ctuconference.friendship.service.FriendshipService;
import cz.ctu.ctuconference.notification.domain.FriendshipNotification;
import cz.ctu.ctuconference.notification.domain.RelationshipAction;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import cz.ctu.ctuconference.notification.service.NotificationConverter;
import cz.ctu.ctuconference.notification.service.NotificationService;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 01.01.2017.
 */
@Service
public class FriendshipCommServiceImpl implements FriendshipCommService {

	@Autowired
	ContactSuggestionService contactSuggestionService;

	@Autowired
	FriendshipService friendshipService;

	@Autowired
	UserService userService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	NotificationConverter notificationConverter;

	@Autowired
	AppMessageSender messageSender;

	@Autowired
	ContactListService contactListService;

	@Autowired
	ContactStateNotifierService contactStateNotifierService;

	@Override
	public void suggestFriendship(UserSession userSession) throws IOException {
		long userId = userSession.getUserId();
		List<FriendDTO> suggestions;
		suggestions = contactSuggestionService.getFriendSuggestions(userId);
		messageSender.send(userSession, "friendship.suggest", new SuggestionsDTO("friendship", suggestions));
	}

	@Override
	public void requestFriendship(UserSession userSession, long requestedUserId) throws IOException {
		friendshipService.requestFriendship(userSession.getUserId(), requestedUserId);
		AppUser user = userService.getById(userSession.getUserId());
		AppUser requestedUser = userService.getById(requestedUserId);
		FriendshipNotification notification = new FriendshipNotification(requestedUser, user, RelationshipAction.REQUESTED);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(requestedUser.getId(), "notification.friendship", notificationDTO);
	}

	@Override
	public void acceptFriendship(UserSession userSession, long acceptedUserId) throws IOException {
		friendshipService.acceptFriendship(userSession.getUserId(), acceptedUserId);
		AppUser user = userService.getById(userSession.getUserId());
		AppUser acceptedUser = userService.getById(acceptedUserId);
		//notify user, that requested the friendship
		FriendshipNotification notification = new FriendshipNotification(acceptedUser, user, RelationshipAction.ACCEPTED);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(acceptedUser.getId(), "notification.friendship", notificationDTO);
		//send contact list update to user
		ContactListItemPrivate contactListItem = contactListService.getFriendItem(user.getId());
		contactStateNotifierService.notifyFriendAdded(contactListItem);
		//send contact list update to user
		ContactListItemPrivate contactListItem2 = contactListService.getFriendItem(acceptedUser.getId());
		contactStateNotifierService.notifyFriendAdded(contactListItem);
	}

	@Override
	public void rejectFriendship(UserSession userSession, long rejectedUserId) throws IOException {
		friendshipService.rejectFriendship(userSession.getUserId(), rejectedUserId);
		AppUser user = userService.getById(userSession.getUserId());
		AppUser rejected = userService.getById(rejectedUserId);
		FriendshipNotification notification = new FriendshipNotification(rejected, user, RelationshipAction.REJECTED);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(rejected.getId(), "notification.friendship", notificationDTO);
	}

	@Override
	public void leaveFriendship(UserSession userSession, long friendId) throws IOException {
		friendshipService.leaveFriendship(userSession.getUserId(), friendId);
		AppUser user = userService.getById(userSession.getUserId());
		AppUser leftFriend = userService.getById(friendId);
		FriendshipNotification notification = new FriendshipNotification(leftFriend, user, RelationshipAction.REMOVED);
		notificationService.addNotification(notification);
		NotificationDTO notificationDTO = notificationConverter.toDTO(notification);
		messageSender.send(leftFriend.getId(), "notification.friendship", notificationDTO);
	}
}
