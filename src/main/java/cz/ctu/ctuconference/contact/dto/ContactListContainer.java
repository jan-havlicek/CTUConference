package cz.ctu.ctuconference.contact.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object for contact list container.
 *
 * Container contains contact grouped by type. It can be:
 * "private" - it contains all private contact (called friends)
 * "multichat" - it contains all multiuser ad hoc created groups of contacts
 * "group" - for each group of contacts there exist one container.
 *
 * Created by Nick Nemame on 25.09.2016.
 */
public class ContactListContainer {

	public static final String TYPE_FRIENDLIST = "private";
	public static final String TYPE_MULTICHAT = "multichat";
	public static final String TYPE_GROUP = "group";

	String type;
	List<ContactListItem> items = new ArrayList<>();

	public ContactListContainer(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ContactListItem> getItems() {
		return items;
	}

	public void setItems(List<ContactListItem> items) {
		this.items = items;
	}

	public void addItem(ContactListItem item) {
		if(items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}
}
