/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation.dao;

import cz.ctu.ctuconference.conversation.domain.Conversation;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;

import java.util.Date;

import org.springframework.stereotype.Component;

/**
 *
 * @author Nick nemame
 */
@Component
public class ConversationDAO extends AbstractDAO<Conversation> {

	public Conversation getConversation(long conversationId, Date oldestMessageDate) {
		String queryString = "SELECT c "
				+ "FROM Conversation c " +
				"JOIN c.messageList m " +
				"JOIN m.sender s " +
				"JOIN m.attachmentList a "
				//+ "WHERE m.dateSent > :oldestMessageDate "
				+ "WHERE c.id = :conversationId";
		Conversation conversation = (Conversation) getEntityManager()
				.createQuery(queryString)
				//.setParameter("olderstMessageDate", oldestMessageDate)
				.setParameter("conversationId", conversationId)
				.getSingleResult();
		return conversation;
	}
//	public List<Message> getConversationMessages(long conversationId, Date oldestMessageDate) {
//		String queryString = "SELECT m " +
//				"FROM Message m " +
//				"JOIN m.conversation c " +
//				"JOIN m.sender s " +
//				//+ "WHERE m.dateSent > :oldestMessageDate "
//				"WHERE c.id = :conversationId";
//		Conversation conversation = (Conversation) getEntityManager()
//				.createQuery(queryString)
//				//.setParameter("olderstMessageDate", oldestMessageDate)
//				.setParameter("conversationId", conversationId)
//				.getSingleResult();
//		return conversation;
//	}

}
