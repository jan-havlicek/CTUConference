package cz.ctu.ctuconference.contact.domain;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public enum ContactState {
	ONLINE(0),
	OFFLINE(1),
	IN_CALL(2),
	DO_NOT_DISTURB(3);

	private int value;

	ContactState(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ""+value;
	}

	public int getValue() {
		return value;
	}
}
