package cz.ctu.ctuconference.attachment.service;

import cz.ctu.ctuconference.attachment.AttachmentUrlBuilder;
import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentUrlListDTO;
import cz.ctu.ctuconference.attachment.service.dao.AttachmentDAO;
import cz.ctu.ctuconference.conversation.domain.Conversation;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.conversation.domain.Message;
import cz.ctu.ctuconference.user.AppUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nick nemame on 13.12.2016.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	AttachmentDAO attachmentDAO;

	@Autowired
	ConversationService conversationService;

	@Autowired
	AttachmentUrlBuilder attachmentUrlBuilder;

	@Override
	public String storeAttachment(Attachment attachment, AppUser sender, long conversationId) {
		String hash = generateFileHash(attachment);
		Message message = new Message(sender, "", new Date());
		attachment.setMessage(message);
		message.addAttachment(attachment);
		attachment.setHash(hash);
		Conversation conversation = conversationService.getById(conversationId);
		conversation.addMessage(attachment.getMessage());
		attachment.getMessage().setConversation(conversation);
		attachmentDAO.persist(attachment);
		attachmentDAO.flush();
		return hash;
	}

	@Override
	public Attachment getAttachment(String hash, long conversationId) {
		Attachment attachment = attachmentDAO.getAttachment(hash);
		if(attachment.getMessage().getConversation().getId() != conversationId) {
			return null;
		}
		return attachment;
	}

	@Override
	public AttachmentUrlListDTO listAttachments(long conversationId) {
		List<Attachment> attachmentList = attachmentDAO.getAllInConversation(conversationId);
		List<String> attachmentUrlList = new ArrayList<>();
		attachmentList.forEach(attachment -> {
			String url = attachmentUrlBuilder.getAttachmentUrl(conversationId, attachment.getHash());
			attachmentUrlList.add(url);
		});
		return new AttachmentUrlListDTO(attachmentUrlList);
	}

	private String generateFileHash(Attachment attachment) {
		String seed = new Date().toString() + attachment.getFileName();
		return DigestUtils.sha256Hex(seed);
	}
}
