package cz.ctu.ctuconference.attachment.service.comm;

import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentUrlListDTO;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by Nick nemame on 03.01.2017.
 */
public interface AttachmentCommService {

	void uploadHandouts(AppUser user, long conversationId, MultipartFile file) throws IOException;

	String uploadPersistentHandouts(AppUser user, long conversationId, MultipartFile file) throws IOException;

	String uploadFile(AppUser user, long conversationId, MultipartFile file) throws IOException;

	Attachment getHandouts(long conversationId);

	AttachmentUrlListDTO listAttachments(long conversationId);

	Attachment getAttachment(String attachmentHash, long conversationId);
}
