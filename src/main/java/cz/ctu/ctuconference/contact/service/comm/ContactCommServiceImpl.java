package cz.ctu.ctuconference.contact.service.comm;

import cz.ctu.ctuconference.contact.dto.ContactFilterDTO;
import cz.ctu.ctuconference.contact.dto.ContactList;
import cz.ctu.ctuconference.contact.service.ContactListService;
import cz.ctu.ctuconference.contact.service.ContactService;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 01.01.2017.
 */
@Service
public class ContactCommServiceImpl implements ContactCommService {

	@Autowired
	AppMessageSender messageSender;

	@Autowired
	ContactListService contactListService;

	@Autowired
	ContactService contactService;

	@Override
	public void getContactList(UserSession userSession) throws IOException {
		long userId = userSession.getUserId();
		ContactList contactList = contactListService.getContactList(userId);
		messageSender.send(userSession, "contact.list", contactList);
	}

	@Override
	public void filterContacts(UserSession userSession, String filter) throws IOException {
		List<ConversationEntityDTO> contacts = contactService.filterContacts(userSession.getUserId(), filter);
		ContactFilterDTO contactFilterDTO = new ContactFilterDTO<>(filter, contacts);
		messageSender.send(userSession, "contact.filter", contactFilterDTO);
	}
}
