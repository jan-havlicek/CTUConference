package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.dto.GroupDTO;

/**
 * This container represents contact list container for particular group.
 * It contains basic group information about group contacts and their conversation.
 *
 * Created by Nick Nemame on 25.09.2016.
 */
public class ContactListContainerGroup extends ContactListContainer {

	/**
	 * group of this container
	 */
	GroupDTO group;
	/**
	 * User's role in this group
	 */
	MembershipRole groupRole;
	/**
	 * Info, whether the user is the only administrator of this group
	 */
	boolean isOnlyAdmin;

	public ContactListContainerGroup(GroupDTO group, MembershipRole role, boolean isOnlyAdmin) {
		super(ContactListContainer.TYPE_GROUP);
		this.group = group;
		this.groupRole = role;
		this.isOnlyAdmin = isOnlyAdmin;
	}

	public GroupDTO getGroup() {
		return group;
	}

	public void setGroup(GroupDTO group) {
		this.group = group;
	}

	public MembershipRole getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(MembershipRole groupRole) {
		this.groupRole = groupRole;
	}

	public boolean isOnlyAdmin() {
		return isOnlyAdmin;
	}

	public void setOnlyAdmin(boolean onlyAdmin) {
		isOnlyAdmin = onlyAdmin;
	}
}
