package cz.ctu.ctuconference.notification.service.dao;

import cz.ctu.ctuconference.notification.domain.GroupMembershipNotification;
import cz.ctu.ctuconference.notification.domain.Notification;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Repository
public class GroupMembershipNotificationDAO extends AbstractDAO<Notification> {

	public List<GroupMembershipNotification> getUnreadNotifications(long userId) {
		String queryString = "SELECT n FROM Notification n " +
				"WHERE n.receiver = :user AND n.isRead = false";
		List<GroupMembershipNotification> notificationList = getEntityManager()
				.createQuery(queryString)
				.setParameter("user", getEntityManager().getReference(AppUser.class, userId))
				.getResultList();
		return notificationList;
	}
}
