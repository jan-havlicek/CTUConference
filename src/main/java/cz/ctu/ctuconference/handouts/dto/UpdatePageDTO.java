package cz.ctu.ctuconference.handouts.dto;

/**
 * Created by Nick nemame on 27.12.2016.
 */
public class UpdatePageDTO {
	long conversationId;
	long page;

	public UpdatePageDTO(long conversationId, long page) {
		this.conversationId = conversationId;
		this.page = page;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}
}
