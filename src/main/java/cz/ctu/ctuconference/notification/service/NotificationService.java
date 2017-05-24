package cz.ctu.ctuconference.notification.service;

import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public interface NotificationService {

	@Transactional
	void addNotification(Notification notification);

	@Transactional(readOnly = true)
	boolean isNotificationReceiver(long notificationId, long receiverId);

	@Transactional
	void markRead(long notificationId);

	@Transactional
	void markUnread(long notificationId);

	@Transactional(readOnly = true)
	List<NotificationDTO> getNotifications(long userId);
}
