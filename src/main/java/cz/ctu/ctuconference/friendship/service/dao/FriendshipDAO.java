/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.friendship.service.dao;

import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nick nemame
 */
@Component
public class FriendshipDAO extends AbstractDAO<Friendship> {
	public Friendship find(long userId, long friendId) {
		String queryString = "SELECT f FROM Friendship f " +
				"WHERE f.user = :user AND f.friend = :friend";
		Friendship friendship = (Friendship) getEntityManager()
				.createQuery(queryString)
				.setParameter("user", getEntityManager().getReference(AppUser.class, userId))
				.setParameter("friend", getEntityManager().getReference(AppUser.class, friendId))
				.getSingleResult();
		return friendship;
	}
}
