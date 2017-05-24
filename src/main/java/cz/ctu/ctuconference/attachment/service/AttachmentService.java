package cz.ctu.ctuconference.attachment.service;

import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentUrlListDTO;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Nick nemame on 13.12.2016.
 */
public interface AttachmentService {

	/**
	 * Store the attachment and create message, that contains this message.
	 * Then return the hash for this file.
	 * @param attachment
	 * @return
	 */
	@Transactional
	String storeAttachment(Attachment attachment, AppUser sender, long conversationId);

	/**
	 * returns selected file
	 * @param hash
	 * @param conversationId
	 * @return
	 */
	@Transactional(readOnly = true)
	Attachment getAttachment(String hash, long conversationId);

	/**
	 * List all files sent in this conversation
	 * @param conversationId
	 * @return
	 */
	@Transactional(readOnly = true)
	AttachmentUrlListDTO listAttachments(long conversationId);
}
