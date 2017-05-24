/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import cz.ctu.ctuconference.authentication.dto.AuthenticationDTO;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.contact.service.comm.ContactStateNotifierService;
import cz.ctu.ctuconference.user.UserDTO;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.*;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;

/**
 *
 * @author Nick Nemame
 */
@Component
public class AuthenticationMessageReceiver implements MessageReceiver {

    @Autowired
    private UserRegistry userRegistry;

	@Autowired
	private AppMessageSender messageSender;

	@Autowired
	private ISocketMessageDataParser parser;

	@Autowired
	private AuthTokenCache authTokenCache;

	@Autowired
	private ContactStateNotifierService notifierService;

	private Gson gson;

	public AuthenticationMessageReceiver() {
		gson = new GsonBuilder().create();
	}

	@Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch (messageType) {
            case "authentication.authorize-socket":
                authorizeUserSocket(jsonMessageData, session);
                break;
			case "authentication.unauthorize-socket":
				unauthorizeUserSocket(session);
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "authentication";
	}

	/**
     * Authorizes web
	 * socket via the token, that was provided to the client during
	 * the authentication process.
     * @param params should contain "user" param with user id
     * @param session
     * @throws IOException
     */
    private void authorizeUserSocket(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
        final String token = parser.getStringParameter(params, "token");
		boolean authorized = false;
		AppUser appUser = authTokenCache.getAuthUserByToken(UUID.fromString(token));
		if(appUser != null) {
			UserSession oldSession;
			if((oldSession = userRegistry.getByUserId(appUser.getId())) != null) {
				deactivateUserSocket(oldSession);
			}
			UserSession userSession = new UserSession(appUser.getId(), appUser.getFullName(), session, messageSender);
			userRegistry.register(userSession);
			notifierService.notifyUserStateChanged(appUser.getId(), ContactState.ONLINE);
			authorized = true;
		}
		UserDTO userDTO = new UserDTO(appUser.getId(), appUser.getEmail(), appUser.getFirstName(), appUser.getLastName(), token.toString());
		AuthenticationDTO authDTO = new AuthenticationDTO(authorized, userDTO);
		messageSender.sendAnonymous(session, "authentication.authorize-socket", authDTO);
    }

	/**
	 * Deactivates old websocket connection. There can be only one live connection per one user.
	 * @param oldSession
	 * @throws IOException
	 */
	private void deactivateUserSocket(UserSession oldSession) throws IOException {
		try {
			if (oldSession.getSession().isOpen()) {
				AuthenticationDTO authDTO = new AuthenticationDTO(true, null);
				messageSender.sendAnonymous(oldSession.getSession(), "authentication.deactivate-socket", authDTO);
			}
		} catch(IOException e) {
			// it is situation when refresh is performed.
		}
		oldSession.close();
		userRegistry.removeBySession(oldSession.getSession());
	}

	/**
	 * Unauthorizes websocket for user that has logged out from the application
	 * (but the session is not closed)
	 * It is necessary to notify other users about user's state change.
	 * @param session
	 * @throws IOException
	 */
	private void unauthorizeUserSocket(WebSocketSession session) throws IOException {
		UserSession userSession = userRegistry.getBySession(session);
		long userId = userSession.getUserId();
		userSession.close();
		userRegistry.removeBySession(session);
		notifierService.notifyUserStateChanged(userId, ContactState.OFFLINE);
		AuthenticationDTO authDTO = new AuthenticationDTO(true, null);
		messageSender.sendAnonymous(session, "authentication.unauthorize-socket", authDTO);
	}
}
