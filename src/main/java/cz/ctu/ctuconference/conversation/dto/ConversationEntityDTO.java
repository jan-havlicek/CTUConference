package cz.ctu.ctuconference.conversation.dto;

/**
 * Basic information about contact (friend, group, group event, group topic etc.)
 * It contains its id and name and conversation information.
 * It is stored in contact list item or container DTOs.
 *
 * Created by Nick nemame on 14.11.2016.
 */
abstract public class ConversationEntityDTO {
	private long id;
	private String name;
	private ConversationDTO conversation;
	protected String type;

	public ConversationEntityDTO(long id, String name, ConversationDTO conversation) {
		this.id = id;
		this.name = name;
		this.conversation = conversation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConversationDTO getConversation() {
		return conversation;
	}

	public void setConversation(ConversationDTO conversation) {
		this.conversation = conversation;
	}
}
