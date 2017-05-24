package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;

import java.util.List;

/**
 * Created by Nick nemame on 27.12.2016.
 */
public class SuggestionsDTO<T extends ConversationEntityDTO> {
	String contactType;
	List<T> suggestions;

	public SuggestionsDTO(String contactType, List<T> suggestions) {
		this.contactType = contactType;
		this.suggestions = suggestions;
	}

	public List<T> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<T> suggestions) {
		this.suggestions = suggestions;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
}
