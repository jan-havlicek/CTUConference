/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.service.dao;

import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author Nick nemame
 */
@Component
public class GroupMembershipDAO extends AbstractDAO<GroupMembership> {

	public GroupMembership getMembership(long userId, long groupId) {
		AppUser user = (AppUser) getEntityManager().getReference(AppUser.class, userId);
		Group group = (Group) getEntityManager().getReference(Group.class, groupId);

		String queryString = "select m from GroupMembership m " +
				"where m.user = :user and m.group = :group";
		List<GroupMembership> resultList = getEntityManager().createQuery(queryString)
				.setParameter("user", user)
				.setParameter("group", group)
				.getResultList();
		if(resultList.size() == 0) return null;
		return resultList.get(0);
	}
}
