package cz.ctu.ctuconference.notification.service;

import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import cz.ctu.ctuconference.notification.service.dao.FriendshipNotificationDAO;
import cz.ctu.ctuconference.notification.service.dao.GroupMembershipNotificationDAO;
import cz.ctu.ctuconference.notification.service.dao.NotificationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	FriendshipNotificationDAO friendshipNotificationDAO;

	@Autowired
	GroupMembershipNotificationDAO groupMembershipNotificationDAO;

	@Autowired
	NotificationDAO notificationDAO;

	@Autowired
	NotificationConverter notificationConverter;

	@Override
	public void addNotification(Notification notification) {
		notificationDAO.persist(notification);
		notificationDAO.flush();
	}

	@Override
	public boolean isNotificationReceiver(long notificationId, long receiverId) {
		Notification notification = notificationDAO.getById(notificationId);
		if(notification == null || notification.getReceiver().getId() != receiverId) return false;
		return true;
	}

	@Override
	public void markRead(long notificationId) {
		notificationDAO.getById(notificationId).setRead(true);
		notificationDAO.flush();
	}

	@Override
	public void markUnread(long notificationId) {
		notificationDAO.getById(notificationId).setRead(false);
		notificationDAO.flush();
	}

	@Override
	public List<NotificationDTO> getNotifications(long userId) {
		List<NotificationDTO> notificationList;
		notificationList = notificationDAO.getUnreadNotifications(userId)
				.stream()
				.map(notification -> notificationConverter.toDTO(notification))
				.collect(Collectors.toList());

		if(notificationList.size() < 10) {
			int limit = 10 - notificationList.size();
			notificationList.addAll(notificationDAO.getReadNotifications(userId, limit)
					.stream()
					.map(notification -> notificationConverter.toDTO(notification))
					.collect(Collectors.toList()));
		}
		return notificationList;
	}
}
