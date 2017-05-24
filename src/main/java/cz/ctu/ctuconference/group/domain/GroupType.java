/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public enum GroupType {
	WORK_TEAM(0),
	SEMINAR_GROUP(1);

	private int value;

	private static final Map<Integer, GroupType> intToTypeMap = new HashMap<>();

	static {
		for (GroupType type : GroupType.values()) {
			intToTypeMap.put(type.value, type);
		}
	}
	GroupType(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ""+value;
	}

	public int getValue() {
		return value;
	}

	public static GroupType fromNumber(long i) {
		GroupType type = intToTypeMap.get(Integer.valueOf((int) i));
		return type;
	}
}

