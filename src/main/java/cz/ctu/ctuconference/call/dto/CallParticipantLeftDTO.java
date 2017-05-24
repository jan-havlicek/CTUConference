package cz.ctu.ctuconference.call.dto;

/**
 * Created by Nick nemame on 26.12.2016.
 */
public class CallParticipantLeftDTO {
	long participantId;

	public CallParticipantLeftDTO(long participantId) {
		this.participantId = participantId;
	}

	public long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}
}
