package cz.ctu.ctuconference.notification.domain;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public enum RelationshipAction {
	REQUESTED(0),
	ACCEPTED(1),
	REJECTED(2),
	REMOVED(3),
	CHANGED_PERMISSIONS(4);

	private int value;

	RelationshipAction(int value) {
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
