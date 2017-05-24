package cz.ctu.ctuconference.call.dto;

/**
 * Created by Nick nemame on 26.12.2016.
 */
public class CallStartedDTO {
	long conversationId;

	public CallStartedDTO(long conversationId) {
		this.conversationId = conversationId;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}
}
