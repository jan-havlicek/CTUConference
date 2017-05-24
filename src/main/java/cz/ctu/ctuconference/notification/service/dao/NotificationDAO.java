package cz.ctu.ctuconference.notification.service.dao;

import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Repository
public class NotificationDAO extends AbstractDAO<Notification> {

	public List<Notification> getUnreadNotifications(long userId) {
		String queryString = "SELECT n FROM Notification n " +
				"WHERE n.receiver = :user AND n.isRead = false";
		List<Notification> notificationList = getEntityManager()
				.createQuery(queryString)
				.setParameter("user", getEntityManager().getReference(AppUser.class, userId))
				.getResultList();
		return notificationList;
	}

	public List<Notification> getReadNotifications(long userId, int limit) {
		String queryString = "SELECT n FROM Notification n " +
				"WHERE n.receiver = :user AND n.isRead = true ORDER BY n.dateCreated DESC";
		List<Notification> notificationList = getEntityManager()
				.createQuery(queryString)
				.setParameter("user", getEntityManager().getReference(AppUser.class, userId))
				.setMaxResults(limit)
				.getResultList();
		return notificationList;
	}
}
