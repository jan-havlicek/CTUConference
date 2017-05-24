/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.user;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.group.domain.GroupEvent;
import cz.ctu.ctuconference.group.domain.GroupTopic;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;

import java.util.List;
import java.util.stream.Collectors;

import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.group.domain.Group;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nick nemame
 */
@Component
public class UserDAO extends AbstractDAO<AppUser> {

	public List<Friendship> getFriendshipList(long userId) {
		String queryString = "select fs " +
				"from Friendship fs " +
				"join fs.user u " +
				"join fs.friend f " +
				"join fs.conversation c " +
				"where u.id = :userId order by f.firstName";
		return getEntityManager().createQuery(queryString).setParameter("userId", userId).getResultList();
	}

	public List<Group> getGroupList(long userId) {
		String queryString = "select u " +
				"from AppUser u " +
				"left join fetch u.groupMembershipList gm " +
				"left join fetch gm.group g " +
				"left join fetch g.conversation gc " +
				"left join fetch g.eventList ge " +
				"left join fetch ge.conversation gec " +
				"left join fetch g.topicList gt " +
				"left join fetch gt.conversation gtc " +
				"where u.id = :userId";
		AppUser user = (AppUser) getEntityManager().createQuery(queryString)
				.setParameter("userId", userId)
				.getSingleResult();
		return user.getGroupMembershipList()
				.stream()
				.filter(membership -> membership.getGroup().isActive() && membership.isAccepted())
				.map(membership -> membership.getGroup())
				.collect(Collectors.toList());
	}

	public List<GroupEvent> getGroupEventList(long userId) {
		return null;
	}

	public List<GroupTopic> getGroupTopicList(long userId) {
		return null;
	}

	public void storeRegistration(AppUser user) {
		getEntityManager().persist(user);
		getEntityManager().flush();
	}

    public AppUser getOneByEmail(String email) {
        List<AppUser> userList = getByProperty("email", email);
        if(userList.isEmpty()) return null;
        return userList.get(0);
    }

    public List<AppUser> getAllByRole(long roleId) {
        String queryString = "select distinct u from AppUser u join u.roleList r where r.id = :roleId order by u.lastName";
        return getEntityManager().createQuery(queryString).setParameter("roleId", roleId).getResultList();
    }

    public List<AppUser> suggestFriendsToUser(long userId) {
		String queryString = "select u from AppUser u " +
				"where u.id not in (" +
					"select f.friend.id from Friendship f " +
						"where f.user.id = :userId)" +
				"and u.id <> :userId";
		return getEntityManager().createQuery(queryString).setParameter("userId", userId).getResultList();
	}

	/**
	 * Returns users which are not in friendship of the user and their name or surname match the filter.
	 * @param userId
	 * @param filter
	 * @return
	 */
	public List<AppUser> getByMatch(long userId, String filter) {
		String queryString = "select u from AppUser u " +
				"where ( u.firstName LIKE :filter OR u.lastName LIKE :filter) AND u.id not in (" +
				"select f.friend.id from Friendship f " +
				"where f.user.id = :userId)";
		return getEntityManager().createQuery(queryString)
				.setParameter("filter", "%" + filter + "%")
				.setParameter("userId", userId)
				.getResultList();
	}
}
