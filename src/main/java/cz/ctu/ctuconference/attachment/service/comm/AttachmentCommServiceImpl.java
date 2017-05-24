package cz.ctu.ctuconference.attachment.service.comm;

import cz.ctu.ctuconference.attachment.service.AttachmentService;
import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentUrlListDTO;
import cz.ctu.ctuconference.attachment.dto.HandoutsAddedDTO;
import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.conversation.service.MessageConverter;
import cz.ctu.ctuconference.conversation.dto.MessageDTO;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Nick nemame on 03.01.2017.
 */
@Service
public class AttachmentCommServiceImpl implements AttachmentCommService {

	@Autowired
	CallManager callManager;

	@Autowired
	AppMessageSender sender;

	@Autowired
	MessageConverter messageConverter;

	@Autowired
	AttachmentService attachmentService;

	/** Upload handouts and attachments **/

	@Override
	public void uploadHandouts(AppUser user, long conversationId, MultipartFile file) throws IOException {
		Attachment attachment = new Attachment(file);
		_setHandoutsToCall(attachment, user, conversationId);
	}

	@Override
	public String uploadPersistentHandouts(AppUser user, long conversationId, MultipartFile file) throws IOException {
		Attachment attachment = new Attachment(file);
		_setHandoutsToCall(attachment, user, conversationId);
		String hash = _storeAttachment(attachment, user, conversationId);
		return hash;
	}

	@Override
	public String uploadFile(AppUser user, long conversationId, MultipartFile file) throws IOException {
		Attachment attachment = new Attachment(file);
		String hash = _storeAttachment(attachment, user, conversationId);
		return hash;
	}

	/** Download attachments and handouts **/

	@Override
	public Attachment getHandouts(long conversationId) {
		Call call = callManager.getCall(conversationId);
		if(call == null) {
			return null;
		}
		Attachment handouts = call.getHandouts();
		return handouts;
	}

	@Override
	public Attachment getAttachment(String attachmentHash, long conversationId) {
		return attachmentService.getAttachment(attachmentHash, conversationId);
	}

	/** List attachments for conversation **/

	@Override
	public AttachmentUrlListDTO listAttachments(long conversationId) {
		return attachmentService.listAttachments(conversationId);
	}

	private Attachment _setHandoutsToCall(Attachment attachment, AppUser user, long conversationId) throws IOException {
		//add handouts to call
		callManager.getCall(conversationId).setHandouts(attachment);

		//send notification with added handouts
		HandoutsAddedDTO message = new HandoutsAddedDTO(1, conversationId);
		sender.sendToCallingParticipants(conversationId, user.getId(), "handouts.added", message, null);

		return attachment;
	}

	private String _storeAttachment(Attachment attachment, AppUser user, long conversationId) throws IOException {
		//store attachment to the conversation
		String hash = attachmentService.storeAttachment(attachment, user, conversationId);

		//send incoming message to all conversation members (except the sender)
		MessageDTO newMessage = messageConverter.toDTO(attachment.getMessage(), false);
		sender.sendToConversationMembers(conversationId, user.getId(), "conversation.incoming-message", newMessage);

		//send incoming message to the sender
		newMessage = messageConverter.toDTO(attachment.getMessage(), true);
		sender.send(user.getId(), "conversation.incoming-message", newMessage);

		return hash;
	}

}
