package cz.ctu.ctuconference.conversation.dto;

/**
 * Created by Nick nemame on 13.02.2017.
 */
public class ConversationMessageDTO {
	private long conversationId;
	private MessageDTO message;

	public ConversationMessageDTO(long conversationId, MessageDTO message) {
		this.conversationId = conversationId;
		this.message = message;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}

	public MessageDTO getMessage() {
		return message;
	}

	public void setMessage(MessageDTO message) {
		this.message = message;
	}
}
