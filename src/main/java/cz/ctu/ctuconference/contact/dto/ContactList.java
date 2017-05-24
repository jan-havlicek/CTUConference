package cz.ctu.ctuconference.contact.dto;

import java.util.List;

/**
 * Data transfer object for contact list. It contains containers of contacts
 *
 * Created by Nick nemame on 29.10.2016.
 */
public class ContactList {
	private List<ContactListContainer> containers;

	public ContactList(List<ContactListContainer> containers) {
		this.containers = containers;
	}

	public List<ContactListContainer> getContainers() {
		return containers;
	}

	public void setContainers(List<ContactListContainer> containers) {
		this.containers = containers;
	}
}
