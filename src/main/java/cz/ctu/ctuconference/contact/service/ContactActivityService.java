package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import org.springframework.stereotype.Service;

/**
 * Created by Nick Nemame on 25.09.2016.
 */
@Service
public interface ContactActivityService {

	void augmentActivityInfo(ConversationEntityDTO conversationEntity, boolean augmentConversationState);

}
