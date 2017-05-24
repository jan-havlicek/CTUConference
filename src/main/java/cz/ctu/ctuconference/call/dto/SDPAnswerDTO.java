package cz.ctu.ctuconference.call.dto;

/**
 * Created by Nick nemame on 26.12.2016.
 */
public class SDPAnswerDTO {
	long participantId;
	String sdpAnswer;

	public SDPAnswerDTO(long participantId, String sdpAnswer) {
		this.participantId = participantId;
		this.sdpAnswer = sdpAnswer;
	}

	public long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(long participantId) {
		this.participantId = participantId;
	}

	public String getSdpAnswer() {
		return sdpAnswer;
	}

	public void setSdpAnswer(String sdpAnswer) {
		this.sdpAnswer = sdpAnswer;
	}
}
