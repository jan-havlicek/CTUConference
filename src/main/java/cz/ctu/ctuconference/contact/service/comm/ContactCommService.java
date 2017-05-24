package cz.ctu.ctuconference.contact.service.comm;

import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.user.UserSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 01.01.2017.
 */
public interface ContactCommService {

	/**
	 * Get contact list and send it to the requester. Contact list consist of contact conteiners
	 * with friends and groups
	 * @param userSession
	 * @throws IOException
	 */
	void getContactList(UserSession userSession) throws IOException;

	/**
	 * Filter contacts, that the user does not have in their contact list.
	 * @param userSession
	 * @param filter
	 * @throws IOException
	 */
	void filterContacts(UserSession userSession, String filter) throws IOException;

}
