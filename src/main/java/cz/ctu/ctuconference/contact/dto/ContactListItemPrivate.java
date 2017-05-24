package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.friendship.dto.FriendDTO;

/**
 * Created by Nick Nemame on 03.10.2016.
 */
public class ContactListItemPrivate extends ContactListItem {

	private FriendDTO friend;

	public ContactListItemPrivate(FriendDTO friend) {
		super();
		this.friend = friend;
	}

	public FriendDTO getFriend() {
		return friend;
	}

	public void setFriend(FriendDTO friend) {
		this.friend = friend;
	}
}
