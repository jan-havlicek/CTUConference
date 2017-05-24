package cz.ctu.ctuconference.friendship.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nick nemame on 26.11.2016.
 */
public interface FriendshipService {
	@Transactional
	void requestFriendship(long userId, long requestingUserId);

	@Transactional
	void acceptFriendship(long userId, long requestingUserId);

	@Transactional
	void rejectFriendship(long userId, long requestingUserId);

	@Transactional
	void leaveFriendship(long userId, long friendId);
}
