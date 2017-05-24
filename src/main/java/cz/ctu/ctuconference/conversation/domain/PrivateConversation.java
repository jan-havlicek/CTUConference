package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.friendship.domain.Friendship;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@DiscriminatorValue("P")
@Table(name="private_conversation")
public class PrivateConversation extends Conversation {

	@OneToMany(mappedBy="conversation")
	List<Friendship> friendshipList;

	@Override
	public List<AppUser> getParticipantList() {
		List<AppUser> participantList = new ArrayList<>();
		for(Friendship friendship : getFriendshipList()) {
			AppUser user = friendship.getUser();
			participantList.add(user);
		}
		return participantList;
	}

	@Override
	public String getContactName() {
		return friendshipList.get(0) + "|" + friendshipList.get(1);
	}

	public List<Friendship> getFriendshipList() {
		return friendshipList;
	}

	public void setFriendshipList(List<Friendship> friendshipList) {
		this.friendshipList = friendshipList;
	}
}
