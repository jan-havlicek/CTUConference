package cz.ctu.ctuconference.friendship.service.comm;

import cz.ctu.ctuconference.user.UserSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 01.01.2017.
 */
public interface FriendshipCommService {

	void suggestFriendship(UserSession userSession) throws IOException;

	void requestFriendship(UserSession userSession, long requestedUserId) throws IOException;

	void acceptFriendship(UserSession userSession, long accepteddUserId) throws IOException;

	void rejectFriendship(UserSession userSession, long rejectedUserId) throws IOException;

	void leaveFriendship(UserSession userSession, long friendId) throws IOException;
}
