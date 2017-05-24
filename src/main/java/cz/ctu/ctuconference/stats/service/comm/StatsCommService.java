package cz.ctu.ctuconference.stats.service.comm;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 14.02.2017.
 */
public interface StatsCommService {
	void gatherStatistics(WebSocketSession session, long conversationId) throws IOException;
}
