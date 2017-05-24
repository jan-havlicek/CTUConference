package cz.ctu.ctuconference.call.dto;

import cz.ctu.ctuconference.call.domain.ParticipantType;

/**
 * Created by Nick nemame on 06.11.2016.
 */
public class ParticipantInfoDTO {
	private long id;
	private boolean videoMuted; //if video transmission from participant is muted
	private boolean audioMuted; //if audio transmission from participant is muted
	private boolean isInitiator; //if the call is webinar, there is one moderator set
	private ParticipantType participantType;

	public ParticipantInfoDTO(long id, boolean videoMuted, boolean audioMuted, boolean isInitiator, ParticipantType participantType) {
		this.id = id;
		this.videoMuted = videoMuted;
		this.audioMuted = audioMuted;
		this.isInitiator = isInitiator;
		this.participantType = participantType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isVideoMuted() {
		return videoMuted;
	}

	public void setVideoMuted(boolean videoMuted) {
		this.videoMuted = videoMuted;
	}

	public boolean isAudioMuted() {
		return audioMuted;
	}

	public void setAudioMuted(boolean audioMuted) {
		this.audioMuted = audioMuted;
	}

	public boolean isInitiator() {
		return isInitiator;
	}

	public void setInitiator(boolean initiator) {
		isInitiator = initiator;
	}

	public ParticipantType getParticipantType() {
		return participantType;
	}

	public void setParticipantType(ParticipantType participantType) {
		this.participantType = participantType;
	}
}
