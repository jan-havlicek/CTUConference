package cz.ctu.ctuconference.utils.communication;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by Nick Nemame on 26.09.2016.
 */
public interface MessageSender {

	void sendErrorMessage(WebSocketSession wsSession, String errorMessage, String incomingMessage) throws IOException;
	void sendAnonymousMessage(WebSocketSession wsSession, SocketMessageDTO message) throws IOException;
	void sendCustomMessage(UserSession userSession, SocketMessageDTO message) throws IOException;
	void sendMessage(UserSession userSession, JsonObject message) throws IOException;

}
