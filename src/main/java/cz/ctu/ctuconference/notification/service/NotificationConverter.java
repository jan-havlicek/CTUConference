package cz.ctu.ctuconference.notification.service;

import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.notification.dto.NotificationDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public interface NotificationConverter {
	@Transactional(readOnly = true)
	NotificationDTO toDTO(Notification notification);
}
