package cz.ctu.ctuconference.call.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick nemame on 03.01.2017.
 */
public enum ParticipantType {
	CALLING_MEMBER(0),
	LECTOR(1),
	CAMERAMAN(2),
	LISTENER(3);

	private int value;

	private static final Map<Integer, ParticipantType> intToTypeMap = new HashMap<>();

	static {
		for (ParticipantType type : ParticipantType.values()) {
			intToTypeMap.put(type.value, type);
		}
	}
	ParticipantType(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ""+value;
	}

	public int getValue() {
		return value;
	}

	public static ParticipantType fromNumber(long i) {
		ParticipantType type = intToTypeMap.get(Long.valueOf(i));
		return type;
	}
}
