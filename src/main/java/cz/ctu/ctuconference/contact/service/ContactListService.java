package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.contact.dto.ContactList;
import cz.ctu.ctuconference.contact.dto.ContactListContainerGroup;
import cz.ctu.ctuconference.contact.dto.ContactListItemPrivate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nick Nemame on 25.09.2016.
 */
@Service
public interface ContactListService {

	@Transactional(readOnly = true)
	ContactList getContactList(long userId);

	@Transactional(readOnly = true)
	ContactListContainerGroup getGroupContainer(long groupId, long receiverId);

	@Transactional(readOnly = true)
	ContactListItemPrivate getFriendItem(Long friendId);
}
