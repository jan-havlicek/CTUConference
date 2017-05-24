package cz.ctu.ctuconference.attachment.dto;

/**
 * Created by Nick nemame on 23.12.2016.
 */
public class HandoutsAddedDTO {
	private long currentPage;
	private long conversationId;

	public HandoutsAddedDTO(long currentPage, long conversationId) {
		this.currentPage = currentPage;
		this.conversationId = conversationId;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}
}
