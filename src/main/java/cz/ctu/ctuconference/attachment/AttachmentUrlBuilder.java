package cz.ctu.ctuconference.attachment;

import org.springframework.stereotype.Component;

/**
 * Created by Nick nemame on 14.12.2016.
 */
@Component
public class AttachmentUrlBuilder {

	public String getAttachmentUrl(long conversationId, String fileHash) {
		return "/api/conversation/" + conversationId + "/attachment/" + fileHash;
	}

	public String getHandoutsUrl(long conversationId) {
		return "/api/conversation/" + conversationId + "/handouts";
	}
}
