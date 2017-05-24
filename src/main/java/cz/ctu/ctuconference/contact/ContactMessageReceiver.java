/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.contact;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.contact.dto.ContactList;
import cz.ctu.ctuconference.contact.dto.SuggestionsDTO;
import cz.ctu.ctuconference.contact.service.ContactListService;
import cz.ctu.ctuconference.contact.service.ContactSuggestionService;
import cz.ctu.ctuconference.contact.service.comm.ContactCommService;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.utils.communication.*;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Nick Nemame
 */
@Component
public class ContactMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	ContactCommService contactCommService;

    @Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
            case "contact.list": //client request contact list
                getContactList(jsonMessageData, session);
                break;
            case "contact.detail": //client request contact detail
                getContactDetail(jsonMessageData, session);
                break;
			case "contact.filter": //vyhledani kontaktu
				filterContact(jsonMessageData, session);
				break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "contact";
	}


	/**
     * @param params
     * @param session
     * @throws IOException
     */
    private void getContactList(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
		contactCommService.getContactList(userSession);
    }

	private void getContactDetail(JsonObject params, WebSocketSession session) {
	}

	private void filterContact(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		String filter = parser.getStringParameter(params, "filter");
		UserSession userSession = getUserSession(session);
		contactCommService.filterContacts(userSession, filter);
	}

}
