package cz.ctu.ctuconference.notification.service.comm;

import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.user.UserSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public interface NotificationCommService {


	void listNotifications(UserSession userSession) throws IOException;

	void markRead(UserSession userSession, long notificationId);

	void markUnread(UserSession userSession, long notificationId);
}
