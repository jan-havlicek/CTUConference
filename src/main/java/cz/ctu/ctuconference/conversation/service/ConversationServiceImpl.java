/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation.service;

import cz.ctu.ctuconference.conversation.domain.Conversation;
import cz.ctu.ctuconference.conversation.domain.Message;
import cz.ctu.ctuconference.conversation.dao.ConversationDAO;
import cz.ctu.ctuconference.conversation.dao.MessageDAO;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserDAO;
import cz.ctu.ctuconference.user.UserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Nick nemame
 */
@Component
public class ConversationServiceImpl implements ConversationService {

	@Autowired
	private ConversationDAO conversationDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private MessageDAO messageDAO;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private MessageConverter messageConverter;

	@Override
	public boolean isMember(long conversationId, long userId) {
		Conversation conversation = conversationDAO.getById(conversationId);
		return conversation.getParticipantList().stream()
			.filter(user -> user.getId() == userId).count() > 0;
	}

	@Override
	public List<Long> getParticipantIdList(long conversationId) {
		Conversation conversation = conversationDAO.getById(conversationId);
		List<AppUser> participantList = conversation.getParticipantList();
		return userConverter.toIdList(participantList);
	}

	@Override
	public Conversation getById(long conversationId) {
		return conversationDAO.getById(conversationId);
	}

	@Override
	public List<MessageDTO> getConversationMessages(long userId, long conversationId) {
		Conversation conversation = conversationDAO.getConversation(conversationId, null);
		List<MessageDTO> messages = conversation.getMessageList()
			.stream().map(message -> messageConverter.toDTO(message, message.getSender().getId() == userId))
			.collect(Collectors.toList());
		return messages;
	}

	@Override
	public long storeMessage(long conversationId, MessageDTO messageDTO) {
		Conversation conversation = conversationDAO.getById(conversationId);
		Message newMessage = new Message(
				userDAO.getById(messageDTO.getSenderId()),
				messageDTO.getMessage(),
				messageDTO.getDateSent()
		);
		newMessage.setConversation(conversation);
		messageDAO.persist(newMessage);
		messageDAO.flush();
		conversation.addMessage(newMessage);
		conversationDAO.flush();
		return newMessage.getId();
	}

/*
@todo when implementing adhoc multichat, this method should be implemented in MultichatCOnversation

	private List<UserDTO> getMultichatParticipants(MultichatConversation conversation) {
		List<AppUser> memberList = conversation.getParticipantList();
		List<UserDTO> memberList = new ArrayList<>();
		for(AppUser participant : memberList) {
			memberList.add(new UserDTO(participant.getId(), "", participant.getFirstName(), participant.getLastName(), ""));
		}
		return memberList;
	}
*/
}
