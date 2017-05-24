package cz.ctu.ctuconference.call.dto;

import org.kurento.client.IceCandidate;

/**
 * Created by Nick nemame on 26.12.2016.
 */
public class IceCandidateDTO {
	private long participantId;
	IceCandidate candidate;

	public IceCandidateDTO(long participantId, IceCandidate candidate) {
		this.participantId = participantId;
		this.candidate = candidate;
	}

	public long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}

	public IceCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(IceCandidate candidate) {
		this.candidate = candidate;
	}
}
