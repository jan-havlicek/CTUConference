package cz.ctu.ctuconference.group.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationDTO;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.group.domain.GroupType;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
public class GroupDTO extends ConversationEntityDTO {

	private GroupType groupType;

	public GroupDTO(long id, String name, GroupType groupType, ConversationDTO conversation) {
		super(id, name, conversation);
		this.groupType = groupType;
		type = "group";
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}
}
