/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.handouts;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.handouts.dto.UpdatePageDTO;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author Nick Nemame
 */
@Component
public class HandoutsMessageReceiver implements MessageReceiver {

    @Autowired
    private UserRegistry registry;

	@Autowired
	private CallManager conversationManager;

	@Autowired
	private AppMessageSender messageSender;

	@Autowired
	private ISocketMessageDataParser parser;

    @Override
    public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws IOException, Exception {
        switch(messageType) {
            case "handouts.update-page":
                updatePage(jsonMessageData, session);
                break;
        }
    }

	@Override
	public String getListenedMessagePrefix() {
		return "handouts";
	}

	private void updatePage(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		UserSession userSession = registry.getBySession(session);
		long userId = userSession.getUserId();
		long newPage = parser.getLongParameter(params, "page");
		if(!userSession.isInCall()) throw new WSMessageException(WSMessageException.NOT_IN_CALL);
		long conversationId = userSession.getUserCall().getConversationId();
		Call call = conversationManager.getCall(conversationId);
		call.setHandoutsPage(newPage);
		UpdatePageDTO updatePageDTO = new UpdatePageDTO(conversationId, newPage);
		messageSender.sendToCallingParticipants(conversationId, userId, "handouts.update-page", updatePageDTO, null);
	}

}
