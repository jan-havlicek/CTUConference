package cz.ctu.ctuconference.notification.service;

import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.notification.domain.FriendshipNotification;
import cz.ctu.ctuconference.notification.domain.GroupMembershipNotification;
import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.notification.dto.FriendshipNotificationDTO;
import cz.ctu.ctuconference.notification.dto.GroupMembershipDTO;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.stereotype.Service;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Service
public class NotificationConverterImpl implements NotificationConverter {

	public NotificationDTO toDTO(Notification notification) {
		if(notification instanceof FriendshipNotification) {
			AppUser friend = ((FriendshipNotification) notification).getFriend();
			return new FriendshipNotificationDTO(
					notification.getId(),
					notification.getDateCreated(),
					notification.isRead(),
					friend.getId(),
					friend.getFullName(),
					((FriendshipNotification) notification).getAction());
		} else if(notification instanceof GroupMembershipNotification) {
			GroupMembership membership = ((GroupMembershipNotification) notification).getMembership();
			return new GroupMembershipDTO(
					notification.getId(),
					notification.getDateCreated(),
					notification.isRead(),
					membership.getUser().getId(),
					membership.getUser().getFullName(),
					membership.getGroup().getId(),
					membership.getGroup().getName(),
					membership.getGroup().getType(),
					((GroupMembershipNotification) notification).getAction(),
					membership.getRole());
		}
		return null;
	}
}
