package cz.ctu.ctuconference.utils.communication;

import cz.ctu.ctuconference.user.UserSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 23.12.2016.
 */
public interface AppMessageSender {

	/**
	 * Send to user, that is not yet authorized
	 * @param session
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendAnonymous(WebSocketSession session, String messageType, Object messageData) throws IOException;

	/**
	 * Send to user defined by their identifier
	 * @param userId
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void send(long userId, String messageType, Object messageData) throws IOException;

	/**
	 * Send to user defined by their user session
	 * @param userSession
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void send(UserSession userSession, String messageType, Object messageData) throws IOException;

	/**
	 * Send to all conversation members
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToConversationMembers(long conversationId, long senderId, String messageType, Object messageData) throws IOException;

	/**
	 * Send to all participants and pending participants of the call
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToCallParticipants(long conversationId, long senderId, String messageType, Object messageData) throws IOException;

	/**
	 * Send to all members of the conversation, that is not in call (it means that is not participants or pending participants
	 * of any other call.
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @param userSentCallback
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToAvailableMembers(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException;

	/**
	 * Send to participants, that are joined to the call (not pending participants). They can be both listeners
	 * or transmitting users.
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @param userSentCallback
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToCallingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException;

	/**
	 * Send to participants, that are in the call and they are transmitting audiovisual stream (it can be also listener,
	 * had rose the hand and is currently transmitting the stream).
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @param userSentCallback
	 */
	void sendToTransmittingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException;

	/**
	 * Send to listeners - participants that are in the call and they are not transmitting any audiovisual data
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @param userSentCallback
	 */
	void sendToListeningParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException;

	/**
	 * Send to participants that are pending
	 * @param conversationId
	 * @param senderId
	 * @param messageType
	 * @param messageData
	 * @param userSentCallback
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToPendingParticipants(long conversationId, long senderId, String messageType, Object messageData, UserSentCallback userSentCallback) throws IOException;

	/**
	 * Send to friends of the user
	 * @param userId
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToFriends(long userId, String messageType, Object messageData) throws IOException;

	/**
	 * Send to all members of all groups, which the user is member
	 * @param userId
	 * @param messageType
	 * @param messageData
	 * @param modifier
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToMyGroupsMembers(long userId, String messageType, Object messageData, GroupMessageModifier modifier) throws IOException;

	/**
	 * Send to all members of the group
	 * @param groupId
	 * @param messageType
	 * @param messageData
	 * @throws IOException
	 */
	@Transactional(readOnly = true)
	void sendToGroupMembers(long groupId, String messageType, Object messageData) throws IOException;
}
