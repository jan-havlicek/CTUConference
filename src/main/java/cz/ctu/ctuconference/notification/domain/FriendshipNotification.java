package cz.ctu.ctuconference.notification.domain;

import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Entity
@DiscriminatorValue("F")
@Table(name="friendship_notification")
public class FriendshipNotification extends Notification {

	@ManyToOne
	@JoinColumn(name = "friend_id")
	private AppUser friend;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="action")
	private RelationshipAction action;

	public FriendshipNotification() {
		super();
	}

	public FriendshipNotification(AppUser receiver, AppUser friend, RelationshipAction action) {
		super(receiver, new Date(), false);
		this.friend = friend;
		this.action = action;
	}

	public AppUser getFriend() {
		return friend;
	}

	public void setFriend(AppUser friend) {
		this.friend = friend;
	}

	public RelationshipAction getAction() {
		return action;
	}

	public void setAction(RelationshipAction action) {
		this.action = action;
	}
}
