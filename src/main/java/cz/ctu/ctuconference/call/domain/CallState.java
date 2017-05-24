package cz.ctu.ctuconference.call.domain;

/**
 * Created by Nick nemame on 28.11.2016.
 */
public enum CallState {
	NONE("none"),
	REQUESTING("requesting"),
	TRANSMITTING("transmitting");

	private String value;

	CallState(String value) {
		this.value = value;
	}

	public static CallState getByName(String name) {
		switch(name) {
			case "none": return NONE;
			case "requesting": return REQUESTING;
			case "transmitting": return TRANSMITTING;
		}
		return null;
	}

	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return value;
	}
}
