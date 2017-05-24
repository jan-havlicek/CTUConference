package cz.ctu.ctuconference.notification.dto;

import cz.ctu.ctuconference.notification.domain.RelationshipAction;

import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public class FriendshipNotificationDTO extends NotificationDTO {
	long friendId;
	String friendName;
	RelationshipAction action;

	public FriendshipNotificationDTO(long id, Date dateCreated, boolean isRead, long friendId, String friendName, RelationshipAction action) {
		super(id, dateCreated, isRead);
		this.friendId = friendId;
		this.friendName = friendName;
		this.action = action;
	}

	public long getFriendId() {
		return friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public RelationshipAction getAction() {
		return action;
	}

	public void setAction(RelationshipAction action) {
		this.action = action;
	}
}
