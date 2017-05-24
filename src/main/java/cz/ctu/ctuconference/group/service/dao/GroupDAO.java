/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.service.dao;

import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author Nick nemame
 */
@Component
public class GroupDAO extends AbstractDAO<Group> {

	public List<Group> suggestGroupsToUser(long userId) {
		String queryString = "select g from Group g " +
				"where g.id not in (" +
				"select g2.id from AppUser u join u.groupMembershipList ms join ms.group g2 where u.id = :userId) " +
				"and g.active = true";
		return getEntityManager().createQuery(queryString).setParameter("userId", userId).getResultList();
	}

	/**
	 * Returns groups the user is not member of and their name matches the filter.
	 * @param filter
	 * @return
	 */
	public List<Group> getByMatch(long userId, String filter) {
		String queryString = "select g from Group g " +
				"where g.name LIKE :filter and g.id not in (" +
				"select g2.id from AppUser u join u.groupMembershipList ms join ms.group g2 where u.id = :userId) " +
				"and g.active = true";
		return getEntityManager().createQuery(queryString)
				.setParameter("filter", "%" + filter + "%")
				.setParameter("userId", userId)
				.getResultList();
	}
}
