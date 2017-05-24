package cz.ctu.ctuconference.conversation.service;

import cz.ctu.ctuconference.attachment.service.AttachmentConverter;
import cz.ctu.ctuconference.attachment.dto.AttachmentDTO;
import cz.ctu.ctuconference.conversation.domain.Message;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick nemame on 14.12.2016.
 */
@Component
public class MessageConverter {

	@Autowired
	AttachmentConverter attachmentConverter;

	/**
	 * Converts message list to DTO. It converts attachments info too.
	 * @param message
	 * @param isSender
	 * @return
	 */
	public MessageDTO toDTO(Message message, boolean isSender) {
		AppUser sender = message.getSender();
		MessageDTO messageDTO = new MessageDTO(
				message.getId(),
				message.getMessage(),
				message.getDateSent(),
				message.isRead(),
				sender.getId(),
				sender.getFullName(),
				isSender ? "outgoing" : "incoming"
		);
		List<AttachmentDTO> attachmentList = new ArrayList<>(message.getAttachmentList().size());
		message.getAttachmentList().forEach(attachment ->
			attachmentList.add(attachmentConverter.toDTO(attachment))
		);
		messageDTO.setAttachmentList(attachmentList);
		return messageDTO;
	}
}
