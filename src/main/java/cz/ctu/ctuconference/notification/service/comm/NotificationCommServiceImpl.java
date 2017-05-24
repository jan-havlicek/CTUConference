package cz.ctu.ctuconference.notification.service.comm;

import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import cz.ctu.ctuconference.notification.service.NotificationConverter;
import cz.ctu.ctuconference.notification.service.NotificationService;
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
public class NotificationCommServiceImpl implements NotificationCommService {

	@Autowired
	AppMessageSender messageSender;

	@Autowired
	NotificationService notificationService;

	@Autowired
	NotificationConverter notificationConverter;

	@Override
	public void listNotifications(UserSession userSession) throws IOException {
		List<NotificationDTO> notificationList = notificationService.getNotifications(userSession.getUserId());
		messageSender.send(userSession, "notification.list", notificationList);
	}

	@Override
	public void markRead(UserSession userSession, long notificationId) {
		if(notificationService.isNotificationReceiver(notificationId, userSession.getUserId())) {
			notificationService.markRead(notificationId);
		}
	}

	@Override
	public void markUnread(UserSession userSession, long notificationId) {
		if(notificationService.isNotificationReceiver(notificationId, userSession.getUserId())) {
			notificationService.markUnread(notificationId);
		}
	}
}
