package cz.ctu.ctuconference.group.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick nemame on 02.01.2017.
 */
public enum MemberPrivilege {
	ADMINISTRATION(0),
	CALL_PERFORMING(1),
	CALL_LISTENING(2),
	BASIC(3);

	private int value;

	MemberPrivilege(int value) {
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
