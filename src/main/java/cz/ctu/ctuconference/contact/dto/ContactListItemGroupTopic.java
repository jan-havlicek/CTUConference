package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.group.dto.GroupTopicDTO;

/**
 * Created by Nick Nemame on 03.10.2016.
 */
public class ContactListItemGroupTopic extends ContactListItem {

	private GroupTopicDTO groupTopic;

	public ContactListItemGroupTopic(GroupTopicDTO groupTopic) {
		this.groupTopic = groupTopic;
	}

	public GroupTopicDTO getGroupTopic() {
		return groupTopic;
	}

	public void setGroupTopic(GroupTopicDTO groupTopic) {
		this.groupTopic = groupTopic;
	}
}
