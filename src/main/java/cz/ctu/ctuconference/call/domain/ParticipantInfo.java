package cz.ctu.ctuconference.call.domain;

/**
 * Created by Nick nemame on 03.01.2017.
 */
public class ParticipantInfo {
	private long id;
	private boolean audioMuted;
	private boolean videoMuted;
	private boolean isInitiator;
	private ParticipantType participantType;

	public ParticipantInfo(long id, boolean audioMuted, boolean videoMuted, boolean isInitiator, ParticipantType participantType) {
		this.id = id;
		this.audioMuted = audioMuted;
		this.videoMuted = videoMuted;
		this.isInitiator = isInitiator;
		this.participantType = participantType;
	}

	void setAsDetachedCameraman() {
		audioMuted = false;
		videoMuted = false;
		participantType = ParticipantType.CAMERAMAN;
	}

	void setAsListener() {
		audioMuted = true;
		videoMuted = true;
		participantType = ParticipantType.LISTENER;
	}

	void setAsLector() {
		audioMuted = false;
		videoMuted = false;
		participantType = ParticipantType.LECTOR;
	}

	void setAsMutedLector() {
		audioMuted = true;
		videoMuted = false;
		participantType = ParticipantType.LECTOR;
	}

	void setAsSpeakingListener() {
		audioMuted = false;
		videoMuted = true;
		participantType = ParticipantType.LISTENER;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/** Getters and setters **/

	public boolean isAudioMuted() {
		return audioMuted;
	}

	public void setAudioMuted(boolean audioMuted) {
		this.audioMuted = audioMuted;
	}

	public boolean isVideoMuted() {
		return videoMuted;
	}

	public void setVideoMuted(boolean videoMuted) {
		this.videoMuted = videoMuted;
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

	public boolean isTransmitting() {
		return !videoMuted || !audioMuted;
	}
}
