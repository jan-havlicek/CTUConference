/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public enum MembershipRole {
	LISTENER(0),
	ORGANIZER(1),
	LECTOR(2),
	ADMIN(3),
	TEAM_MEMBER(4);

	private int value;

	private static final Map<Integer, MembershipRole> intToTypeMap = new HashMap<>();

	private static final Map<MembershipRole, List<MemberPrivilege>> rolePrivileges = new HashMap<>();

	static {
		for (MembershipRole type : MembershipRole.values()) {
			intToTypeMap.put(type.value, type);
		}
		List<MemberPrivilege> adminPrivileges = new ArrayList<>();
		adminPrivileges.add(MemberPrivilege.ADMINISTRATION);
		adminPrivileges.add(MemberPrivilege.CALL_PERFORMING);
		adminPrivileges.add(MemberPrivilege.CALL_LISTENING);
		adminPrivileges.add(MemberPrivilege.BASIC);
		rolePrivileges.put(MembershipRole.ADMIN, adminPrivileges);

		List<MemberPrivilege> lectorPrivileges = new ArrayList<>();
		lectorPrivileges.add(MemberPrivilege.CALL_PERFORMING);
		lectorPrivileges.add(MemberPrivilege.CALL_LISTENING);
		lectorPrivileges.add(MemberPrivilege.BASIC);
		rolePrivileges.put(MembershipRole.LECTOR, lectorPrivileges);

		List<MemberPrivilege> listenerPrivileges = new ArrayList<>();
		listenerPrivileges.add(MemberPrivilege.CALL_LISTENING);
		listenerPrivileges.add(MemberPrivilege.BASIC);
		rolePrivileges.put(MembershipRole.LISTENER, listenerPrivileges);

		List<MemberPrivilege> teamMemberPrivileges = new ArrayList<>();
		teamMemberPrivileges.add(MemberPrivilege.CALL_PERFORMING);
		teamMemberPrivileges.add(MemberPrivilege.CALL_LISTENING);
		teamMemberPrivileges.add(MemberPrivilege.BASIC);
		rolePrivileges.put(MembershipRole.TEAM_MEMBER, teamMemberPrivileges);
	}

	MembershipRole(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return ""+value;
	}

	public int getValue() {
		return value;
	}

	public boolean hasPrivilege(MemberPrivilege privilege) {
		return rolePrivileges.get(this).stream().filter(item -> item == privilege).count() > 0;
	}

	public static MembershipRole fromNumber(long i) {
		MembershipRole type = intToTypeMap.get(Long.valueOf(i));
		return type;
	}
}

