package cz.ctu.ctuconference.contact.dto;

import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;

import java.util.List;

/**
 * Created by Nick nemame on 27.12.2016.
 */
public class ContactFilterDTO<T extends ConversationEntityDTO> {
	String filter;
	List<T> results;

	public ContactFilterDTO(String contactType, List<T> results) {
		this.filter = contactType;
		this.results = results;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public String getContactType() {
		return filter;
	}

	public void setContactType(String filter) {
		this.filter = filter;
	}
}
