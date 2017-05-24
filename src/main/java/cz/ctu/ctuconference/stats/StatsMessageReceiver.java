package cz.ctu.ctuconference.stats;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.notification.service.comm.NotificationCommService;
import cz.ctu.ctuconference.stats.service.comm.StatsCommService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Component
public class StatsMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	StatsCommService statsCommService;

	@Override
	public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws WSMessageException, IOException, Exception {
		switch(messageType) {
			case "stats.gather":
				gatherStatistics(jsonMessageData, session);
				break;
		}
	}

	@Override
	public String getListenedMessagePrefix() {
		return "stats";
	}

	private void gatherStatistics(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		long conversationId = parser.getLongParameter(params, "conversationId");
		statsCommService.gatherStatistics(session, conversationId);
	}

}
