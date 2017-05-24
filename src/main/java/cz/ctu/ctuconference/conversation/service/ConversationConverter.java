package cz.ctu.ctuconference.conversation.service;

import cz.ctu.ctuconference.conversation.domain.Conversation;
import cz.ctu.ctuconference.conversation.dto.ConversationDTO;
import cz.ctu.ctuconference.user.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Nick nemame on 15.11.2016.
 */
@Component
public class ConversationConverter {

	@Autowired
	UserConverter userConverter;

	public ConversationDTO toDTO(Conversation conversation) {
		ConversationDTO conversationDTO = new ConversationDTO(
				conversation.getId(),
				conversation.getName(),
				userConverter.toDTOList(conversation.getParticipantList())
		);
		return conversationDTO;
	}
}
