package cz.ctu.ctuconference.call.domain;

import cz.ctu.ctuconference.user.UserSession;
import org.kurento.client.MediaPipeline;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 04.01.2017.
 */
public class WebinarCall extends Call {

	public WebinarCall(long initiatorId, String initiatorName, long conversationId, String callName, List<Long> members, MediaPipeline pipeline) throws IOException {
		super(initiatorId, initiatorName, conversationId, callName, members, pipeline);
	}

	@Override
	public CallType getCallType() {
		return CallType.WEBINAR;
	}

	public List<UserSession> getLectors() {
		return getParticipants().stream().filter(userSession -> isLector(userSession)).collect(Collectors.toList());
	}

	public boolean hasMoreLectors() {
		return getParticipants().stream().filter(userSession -> isLector(userSession)).count() > 1;
	}

	public UserSession getDetachedCameraman() {
		return getParticipants().stream().filter(userSession -> isDetachedCameraman(userSession)).findFirst().orElseGet(() -> null);
	}

	public boolean hasDetachedCameraman() {
		return getParticipants().stream().filter(userSession -> isDetachedCameraman(userSession)).count() > 0;
	}

	public boolean isLector(UserSession userSession) {
		return userSession.isInCall() && userSession.getUserCall().getParticipantInfo().getParticipantType() == ParticipantType.LECTOR;
	}

	public boolean isDetachedCameraman(UserSession userSession) {
		return userSession.isInCall() && userSession.getUserCall().getParticipantInfo().getParticipantType() == ParticipantType.CAMERAMAN;
	}

}
