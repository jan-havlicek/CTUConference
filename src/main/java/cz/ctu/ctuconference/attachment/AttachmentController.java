package cz.ctu.ctuconference.attachment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.attachment.dto.AttachmentUrlListDTO;
import cz.ctu.ctuconference.attachment.service.comm.AttachmentCommService;
import cz.ctu.ctuconference.authentication.AuthenticationService;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;

/**
 * Created by Nick nemame on 12.12.2016.
 */
@Controller
@RequestMapping("/api")
 public class AttachmentController {

	@Autowired
	AttachmentCommService attachmentCommService;

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	ConversationService conversationService;

	@Autowired
	AttachmentUrlBuilder urlBuilder;

	private Gson gson = new GsonBuilder().create();

	/**
	 * Create new attachment and set it to the active call.
	 * Broadcast the notification to all call participants.
	 */
	@RequestMapping(path = "/conversation/{conversationId}/handouts", method = RequestMethod.POST)
	public ResponseEntity<String> uploadHandouts(@PathVariable long conversationId, @RequestParam("file") MultipartFile file) throws IOException {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedResponse();
		}
		attachmentCommService.uploadHandouts(user, conversationId, file);
		return getSuccessResponse();
	}

	/**
	 * Create new attachment and set it to the active call.
	 * Broadcast the notification to all call participants.
	 * Store the attachment to the new message and
	 * broadcast this new message to all conversation members.
	 */
	@RequestMapping(path = "/conversation/{conversationId}/handouts/persistent", method = RequestMethod.POST)
	public ResponseEntity<String> uploadPersistentHandouts(@PathVariable long conversationId, @RequestParam("file") MultipartFile file) throws IOException {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedResponse();
		}
		String hash = attachmentCommService.uploadPersistentHandouts(user, conversationId, file);
		return getCreatedResponse(urlBuilder.getAttachmentUrl(conversationId, hash));
	}

	/**
	 * Download the handouts for active call
	 */
	@RequestMapping(path = "/conversation/{conversationId}/handouts", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadHandouts(@PathVariable long conversationId) {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedFileResponse();
		}
		Attachment handouts = attachmentCommService.getHandouts(conversationId);
		if(handouts == null) {
			getNotFoundResponse();
		}
		return getFileResponse(handouts, true);
	}

	/**
	 * Return list of URL of attachments attached to the requested conversation.
	 * It will send JSON containing "attachments" field.
	 */
	@RequestMapping(path = "/conversation/{conversationId}/attachments", method = RequestMethod.GET)
	public ResponseEntity<String> listConversationAttachment(@PathVariable long conversationId) {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedResponse();
		}
		AttachmentUrlListDTO attachmentUrlList = attachmentCommService.listAttachments(conversationId);
		JsonObject message = new JsonObject();
		message.add("attachments", gson.toJsonTree(attachmentUrlList));
		return getSuccessResponse(message.toString());
	}

	/**
	 * Create new attachment and store it to the conversation into a new message.
	 * broadcast this new message to all conversation members.
	 */
	@RequestMapping(path = "/conversation/{conversationId}/attachment", method = RequestMethod.POST)
	public ResponseEntity<String> uploadAttachment(@PathVariable long conversationId, @RequestBody MultipartFile file) throws IOException {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedResponse();
		}
		String hash = attachmentCommService.uploadFile(user, conversationId, file);
		return getCreatedResponse(urlBuilder.getAttachmentUrl(conversationId, hash));
	}

	/**
	 * Return specified attachment. If not exists, 404 is sent.
	 */
	@RequestMapping(path = "conversation/{conversationId}/attachment/{attachmentHash}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadAttachment(@PathVariable long conversationId, @PathVariable String attachmentHash) {
		AppUser user = getAuthorization(conversationId);
		if(user == null) {
			return getUnauthorizedFileResponse();
		}
		Attachment attachment = attachmentCommService.getAttachment(attachmentHash, conversationId);
		if(attachment == null) {
			getNotFoundResponse();
		}
		return getFileResponse(attachment, true);
	}

	/**
	 * Check authentication and authorize user to the conversation
	 */
	private AppUser getAuthorization(long conversationId) {
		AppUser user = authenticationService.getLoggedUser();
		if(user == null || !conversationService.isMember(conversationId, user.getId())) {
			return null;
		}
		return user;
	}

	/** Response shorthands **/

	private ResponseEntity<String> getCreatedResponse(String newUrl) {
		return ResponseEntity.created(URI.create(newUrl)).body("");
	}

	private ResponseEntity<String> getSuccessResponse(String message) {
		return ResponseEntity.ok(message);
	}

	private ResponseEntity<String> getSuccessResponse() {
		return getSuccessResponse("");
	}

	private ResponseEntity<String> getUnauthorizedResponse() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
	}

	private ResponseEntity<String> getNotFoundResponse() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
	}

	private ResponseEntity<byte[]> getUnauthorizedFileResponse() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new byte[0]);
	}

	private ResponseEntity<byte[]> getFileResponse(Attachment attachment, boolean isHandouts) {
		String disposition = isHandouts ? "inline" : "attachment";
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, disposition+"; filename=\""+ attachment.getFileName()+"\"")
				.header(HttpHeaders.CONTENT_LENGTH, attachment.getFileData().length+"")
				.header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
				.body(attachment.getFileData());
	}

	private void validateHandouts(MultipartFile handouts) {
		if (!handouts.getContentType().equals("image/jpeg")) {
			throw new RuntimeException("Only JPG images are accepted");
		}
	}
}
