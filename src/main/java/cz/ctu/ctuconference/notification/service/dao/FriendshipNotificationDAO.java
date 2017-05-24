package cz.ctu.ctuconference.notification.service.dao;

import cz.ctu.ctuconference.notification.domain.FriendshipNotification;
import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Repository
public class FriendshipNotificationDAO extends AbstractDAO<FriendshipNotification> {

	public List<FriendshipNotification> getUnreadNotifications(long userId) {
		String queryString = "SELECT n FROM FriendshipNotification n " +
				"WHERE n.receiver = :user AND n.isRead = false";
		List<FriendshipNotification> notificationList = getEntityManager()
				.createQuery(queryString)
				.setParameter("user", getEntityManager().getReference(AppUser.class, userId))
				.getResultList();
		return notificationList;
	}
}
