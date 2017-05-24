package cz.ctu.ctuconference.stats.service.comm;

import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.kurento.client.Stats;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick nemame on 14.02.2017.
 */
@Service
public class StatsCommServiceImpl implements StatsCommService {

	@Autowired
	CallManager callManager;

	@Autowired
	AppMessageSender sender;

	public void gatherStatistics(WebSocketSession session, long conversationId) throws IOException {
		Call call = callManager.getCall(conversationId);
		Map<String, Map<String, Stats>> statistics = new HashMap<>();
		call.getParticipants().stream().forEach(participant -> {
					statistics.put(
							participant.getUserName(),
							participant.getUserCall().getOutgoingMediaEndpoint() != null ? participant.getUserCall().getOutgoingMediaEndpoint().getStats() : null
					);
					Map<Long, WebRtcEndpoint> e = participant.getUserCall().getIncomingMediaEndpoints();
					e.forEach((p, val) ->
						statistics.put(participant.getUserName() + " - " + p,
								val.getStats())
					);
				}
			);

		sender.sendAnonymous(session, "stats.gather", statistics);
	}
}
