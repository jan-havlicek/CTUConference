package cz.ctu.ctuconference.call.domain;

/**
 * Created by Nick nemame on 06.11.2016.
 */
public enum CallType {
	VOICE_CALL("voice"),
	VIDEO_CALL("video"),
	WEBINAR("webinar");

	private String value;

	CallType(String value) {
		this.value = value;
	}

	public static CallType getByName(String name) {
		switch(name) {
			case "voice": return VOICE_CALL;
			case "video": return VIDEO_CALL;
			case "webinar": return WEBINAR;
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getValue() {
		return value;
	}
}
