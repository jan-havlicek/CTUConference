
package cz.ctu.ctuconference.conversation.service;

import java.util.List;

import cz.ctu.ctuconference.conversation.domain.Conversation;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nick nemame
 */
@Component
public interface ConversationService {

	/**
	 * Get Conversation entity by id
	 * @param conversationId
	 * @return
	 */
	@Transactional(readOnly = true)
	Conversation getById(long conversationId);

	/**
	 * Get Conversation messages for particular conversation.
	 * UserId parameter is used to determine whether the message is incoming or outgoing
	 * @param userId
	 * @param conversationId
	 * @return
	 */
	@Transactional(readOnly=true)
	List<MessageDTO> getConversationMessages(long userId, long conversationId);

	/**
	 * Stores new message to the conversation.
	 * @param conversationId
	 * @param messageDTO
	 * @return
	 */
	@Transactional
	public long storeMessage(long conversationId, MessageDTO messageDTO);

	/**
	 * Check, if the user is member
	 * @param conversationId
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	boolean isMember(long conversationId, long userId);

	/**
	 * Get participant list of their ids. It is used when invoking the call
	 * (there is only need to know the ids).
	 * @param conversationId
	 * @return
	 */
	@Transactional(readOnly = true)
	List<Long> getParticipantIdList(long conversationId);

}
