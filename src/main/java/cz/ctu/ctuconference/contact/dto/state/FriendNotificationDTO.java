package cz.ctu.ctuconference.contact.dto.state;

import cz.ctu.ctuconference.contact.dto.ContactListItemPrivate;
import cz.ctu.ctuconference.contact.domain.ContactAction;
import cz.ctu.ctuconference.contact.domain.ContactState;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class FriendNotificationDTO {
	private Long friendId;
	private ContactAction action;
	private ContactState state;
	private ContactListItemPrivate friendItem;

	public FriendNotificationDTO(Long friendId, ContactAction action, ContactState state) {
		this.friendId = friendId;
		this.action = action;
		this.state = state;
		this.friendItem = null;
	}

	public FriendNotificationDTO(ContactListItemPrivate friendItem) {
		this.friendId = null;
		this.action = ContactAction.ADDED;
		this.state = ContactState.ONLINE;
		this.friendItem = friendItem;
	}

	public ContactAction getAction() {
		return action;
	}

	public void setAction(ContactAction action) {
		this.action = action;
	}

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public ContactState getState() {
		return state;
	}

	public void setState(ContactState state) {
		this.state = state;
	}

	public ContactListItemPrivate getFriendItem() {
		return friendItem;
	}

	public void setFriendItem(ContactListItemPrivate friendItem) {
		this.friendItem = friendItem;
	}
}
