package cz.ctu.ctuconference.friendship.service;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.conversation.domain.PrivateConversation;
import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.friendship.service.dao.FriendshipDAO;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Nick nemame on 22.12.2016.
 */
@Service
public class FriendshipServiceImpl implements FriendshipService {

	@Autowired
	FriendshipDAO friendshipDAO;

	@Autowired
	UserDAO userDAO;

	@Override
	public void requestFriendship(long userId, long requestedUserId) {
		AppUser user = userDAO.getById(userId);
		AppUser requestedUser = userDAO.getById(requestedUserId);
		PrivateConversation conversation = new PrivateConversation();
		conversation.setName("");
		Friendship friendship = new Friendship(user, requestedUser, conversation, ContactAuthState.REQUESTED, new Date());
		Friendship inverted = new Friendship(requestedUser, user, conversation, ContactAuthState.WAITING, new Date());
		// @todo add addFriendship() call
		friendshipDAO.persist(friendship);
		friendshipDAO.persist(inverted);
		friendshipDAO.flush();
	}

	@Override
	public void acceptFriendship(long userId, long requestingUserId) {
		friendshipDAO.find(userId, requestingUserId).setState(ContactAuthState.ACCEPTED);
		friendshipDAO.find(requestingUserId, userId).setState(ContactAuthState.ACCEPTED);
		friendshipDAO.flush();
	}

	@Override
	public void rejectFriendship(long userId, long requestingUserId) {
		friendshipDAO.find(userId, requestingUserId).setState(ContactAuthState.REJECTED);
		friendshipDAO.find(requestingUserId, userId).setState(ContactAuthState.REJECTED);
		friendshipDAO.flush();
	}

	@Override
	public void leaveFriendship(long userId, long friendId) {
		friendshipDAO.find(userId, friendId).setState(ContactAuthState.REMOVED);
		friendshipDAO.find(friendId, userId).setState(ContactAuthState.REMOVED);
		friendshipDAO.flush();
	}
}
