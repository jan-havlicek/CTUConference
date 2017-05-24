/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.utils.communication;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 *
 * @author Nick Nemame
 */
@Component
public interface MessageReceiver {
    void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws WSMessageException, IOException, Exception;
	String getListenedMessagePrefix();
}
