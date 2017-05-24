package cz.ctu.ctuconference.contact.dto.state;

import cz.ctu.ctuconference.contact.dto.ContactListContainerGroup;
import cz.ctu.ctuconference.contact.domain.ContactAction;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class GroupNotificationDTO {
	protected Long groupId;
	protected ContactAction action;
	protected ContactListContainerGroup groupContainer;


	public GroupNotificationDTO(Long groupId, ContactAction action) {
		this.groupId = groupId;
		this.action = action;
		this.groupContainer = null;
	}

	public GroupNotificationDTO(ContactListContainerGroup groupContainer) {
		this.groupId = null;
		this.action = ContactAction.ADDED;
		this.groupContainer = groupContainer;
	}


	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public ContactAction getAction() {
		return action;
	}

	public void setAction(ContactAction action) {
		this.action = action;
	}
}
