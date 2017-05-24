package cz.ctu.ctuconference.contact.domain;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public enum ContactAction {
	ADDED(0),
	REMOVED(1),
	STATE_CHANGED(2);

	private int value;

	ContactAction(int value) {
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
