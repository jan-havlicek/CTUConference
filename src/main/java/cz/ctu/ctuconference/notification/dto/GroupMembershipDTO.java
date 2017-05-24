package cz.ctu.ctuconference.notification.dto;

import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.notification.domain.RelationshipAction;

import java.util.Date;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public class GroupMembershipDTO extends NotificationDTO {
	long userId;
	String userName;
	long groupId;
	String groupName;
	GroupType groupType;
	RelationshipAction action;
	MembershipRole role;

	public GroupMembershipDTO(long id, Date dateCreated, boolean isRead, long userId, String userName, long groupId, String groupName, GroupType groupType, RelationshipAction action, MembershipRole role) {
		super(id, dateCreated, isRead);
		this.userId = userId;
		this.userName = userName;
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupType = groupType;
		this.action = action;
		this.role = role;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public RelationshipAction getAction() {
		return action;
	}

	public void setAction(RelationshipAction action) {
		this.action = action;
	}

	public MembershipRole getRole() {
		return role;
	}

	public void setRole(MembershipRole role) {
		this.role = role;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}
}
