package cz.ctu.ctuconference.attachment.service;

import cz.ctu.ctuconference.attachment.AttachmentUrlBuilder;
import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Nick nemame on 14.12.2016.
 */
@Component
public class AttachmentConverter {

	@Autowired
	AttachmentUrlBuilder urlBuilder;

	/**
	 * Returns DTO for attachment. It contains only metadata for the
	 * file (name, type and download URL). It is necessary to download
	 * it to get content of the file.
	 * @param attachment
	 * @return
	 */
	public AttachmentDTO toDTO(Attachment attachment) {
		AttachmentDTO attachmentDTO = new AttachmentDTO(
			attachment.getFileName(),
			attachment.getContentType(),
			urlBuilder.getAttachmentUrl(
					attachment.getMessage().getConversation().getId(),
					attachment.getHash()
			)
		);
		return attachmentDTO;
	}
}

