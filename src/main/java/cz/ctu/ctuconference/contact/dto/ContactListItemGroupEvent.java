package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.group.dto.GroupEventDTO;

/**
 * Data transfer object for contact type "Group event". This "contact" represents events
 * such as webinar, repeating meetings, that can be displayed in calendar.
 * Information about the date of the event is stored in Group event DTO.
 *
 * Created by Nick Nemame on 03.10.2016.
 */
public class ContactListItemGroupEvent extends ContactListItem {

	private GroupEventDTO groupEvent;

	public ContactListItemGroupEvent(GroupEventDTO groupEvent) {
		this.groupEvent = groupEvent;
	}

	public GroupEventDTO getGroupEvent() {
		return groupEvent;
	}

	public void setGroupEvent(GroupEventDTO groupEvent) {
		this.groupEvent = groupEvent;
	}
}
