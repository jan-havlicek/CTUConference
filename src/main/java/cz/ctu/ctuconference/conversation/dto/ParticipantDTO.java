package cz.ctu.ctuconference.conversation.dto;

/**
 * Information about participant of conversation.
 * Beside the basic information used in chat (name, avatar etc.), it contains also activity information
 *
 * Created by Nick nemame on 15.11.2016.
 */
public class ParticipantDTO {

	private long id;

	private String firstName;

	private String lastName;

	private String avatar;

	private boolean isOnline;

	private boolean isTransmitting;

	private boolean isDoNotDisturb;

	public ParticipantDTO(long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	public boolean isTransmitting() {
		return isTransmitting;
	}

	public void setTransmitting(boolean transmitting) {
		isTransmitting = transmitting;
	}

	public boolean isDoNotDisturb() {
		return isDoNotDisturb;
	}

	public void setDoNotDisturb(boolean doNotDisturb) {
		isDoNotDisturb = doNotDisturb;
	}
}
