package cz.ctu.ctuconference.notification.domain;

import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Entity
@DiscriminatorValue("G")
@Table(name="group_membership_notification")
public class GroupMembershipNotification extends Notification {

	@ManyToOne
	@JoinColumn(name = "group_membership_id")
	private GroupMembership membership;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="action")
	private RelationshipAction action;

	public GroupMembershipNotification() {
		super();
	}

	public GroupMembershipNotification(AppUser receiver, GroupMembership membership, RelationshipAction action) {
		super(receiver, new Date(), false);
		this.membership = membership;
		this.action = action;
	}

	public GroupMembership getMembership() {
		return membership;
	}

	public void setMembership(GroupMembership membership) {
		this.membership = membership;
	}

	public RelationshipAction getAction() {
		return action;
	}

	public void setAction(RelationshipAction action) {
		this.action = action;
	}
}
