/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.attachment.service.dao;

import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.utils.dao.AbstractDAO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author Nick nemame
 */
@Component
public class AttachmentDAO extends AbstractDAO<Attachment> {

    public Attachment getAttachment(String hash) {
		List<Attachment> attachmentList = this.getByProperty("hash", hash);
		if(attachmentList.size() == 0) return null;
		return attachmentList.get(0);
    }

	public List<Attachment> getAllInConversation(long conversationId) {
		String query = "select a from Attachment a " +
				"JOIN a.message m " +
				"JOIN m.conversation c " +
				"WHERE c.id = :conversationId";
		return this.getEntityManager().createQuery(query)
				.setParameter("conversationId", conversationId).getResultList();
	}
}
