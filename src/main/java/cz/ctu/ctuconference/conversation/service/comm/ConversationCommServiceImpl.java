package cz.ctu.ctuconference.conversation.service.comm;

import cz.ctu.ctuconference.conversation.dto.ConversationMessageDTO;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 05.02.2017.
 */
@Service
public class ConversationCommServiceImpl implements ConversationCommService {

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private AppMessageSender messageSender;

	@Override
	public void getConversationMessages(UserSession userSession, long conversationId) throws IOException {
		long userId = userSession.getUserId();
		List<MessageDTO> messages = conversationService.getConversationMessages(userId, conversationId);
		messageSender.send(userSession, "conversation.messages", messages);
	}

	@Override
	public void handleIncomingMessage(UserSession userSession, long conversationId, MessageDTO messageDTO) throws IOException {
		long userId = userSession.getUserId();
		long messageId = conversationService.storeMessage(conversationId, messageDTO);
		messageDTO.setId(messageId);
		ConversationMessageDTO conversationMessageDTO = new ConversationMessageDTO(conversationId, messageDTO);
		messageSender.sendToConversationMembers(conversationId, userId, "conversation.incoming-message", conversationMessageDTO);
	}

}
