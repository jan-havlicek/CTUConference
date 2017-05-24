package cz.ctu.ctuconference.conversation.service.comm;

import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.user.UserSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 05.02.2017.
 */
public interface ConversationCommService {

	/**
	 * Get all messages that belongs to specified conversation id.
	 * @param userSession
	 * @param conversationId
	 * @throws IOException
	 */
	void getConversationMessages(UserSession userSession, long conversationId) throws IOException;

	/**
	 * Store incoming message and send it to all active members of conversation. The conversation
	 * is also sent in order to filter the messages for the chat with given conversation id.
	 * @param userSession
	 * @param conversationId
	 * @param messageDTO
	 * @throws IOException
	 */
	void handleIncomingMessage(UserSession userSession, long conversationId, MessageDTO messageDTO) throws IOException;
}
