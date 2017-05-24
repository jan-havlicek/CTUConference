package cz.ctu.ctuconference.group.service.comm;

import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public interface GroupCommService {


	void getMemberships(UserSession userSession, long groupId) throws IOException;

	void createGroup(UserSession creatorSession, String name, GroupType type) throws IOException;

	void removeGroup(UserSession userSession, long groupId) throws IOException;

	void suggestGroups(UserSession userSession) throws IOException;

	void requestMembership(UserSession userSession, long groupId) throws IOException;

	/**
	 * Administrator will accept membership of the user and then send to the user two messages.
	 * First updates the contact list and the second is notification about new group added.
	 * @param adminSession
	 * @param userId
	 * @param groupId
	 * @param role
	 * @throws IOException
	 */
	void acceptMembership(UserSession adminSession, long userId, long groupId, MembershipRole role) throws IOException;

	void rejectMembership(UserSession adminSession, long userId, long groupId) throws IOException;

	void removeMembership(UserSession adminSession, long userId, long groupId) throws IOException;

	void changeRole(UserSession adminSession, long userId, long groupId, MembershipRole role) throws IOException;

	void leaveGroup(UserSession userSession, long groupId) throws IOException;
}
