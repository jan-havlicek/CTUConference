package cz.ctu.ctuconference.group.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationDTO;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
public class GroupTopicDTO extends ConversationEntityDTO {
	public GroupTopicDTO(long id, String name, ConversationDTO conversation) {
		super(id, name, conversation);
		type = "group-topic";
	}
}
