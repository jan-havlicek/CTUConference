package cz.ctu.ctuconference.friendship.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationDTO;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;

/**
 * Created by Nick nemame on 15.11.2016.
 */
public class FriendDTO extends ConversationEntityDTO {
	public FriendDTO(long id, String name, ConversationDTO conversation) {
		super(id, name, conversation);
		type = "friend";
	}

}
