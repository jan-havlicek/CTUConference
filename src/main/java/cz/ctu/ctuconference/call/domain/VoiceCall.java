package cz.ctu.ctuconference.call.domain;

import org.kurento.client.MediaPipeline;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 04.01.2017.
 */
public class VoiceCall extends Call {

	public VoiceCall(long initiatorId, String initiatorName, long conversationId, String callName, List<Long> members, MediaPipeline pipeline) throws IOException {
		super(initiatorId, initiatorName, conversationId, callName, members, pipeline);
	}

	@Override
	public CallType getCallType() {
		return CallType.VOICE_CALL;
	}
}
