import {MembershipRole} from "./membership-role.enum";
import {MembershipState} from "./membership-state.enum";
export class GroupMembership {
	private _userId: number;
	private _userName: string;
	private _email: string;
	private _role: MembershipRole;
	private _state: MembershipState;

	constructor(userId: number, userName: string, email: string, role: MembershipRole, state: MembershipState) {
		this._userId = userId;
		this._userName = userName;
		this._email = email;
		this._role = role;
		this._state = state;
	}

	public static fromJson(jsonObject): GroupMembership {
		let membership = new GroupMembership(
			jsonObject.userId,
			jsonObject.firstName + " " + jsonObject.lastName,
			jsonObject.email,
			jsonObject.role ? MembershipRole.get(jsonObject.role + "") : null,
			MembershipState.get(jsonObject.state + "")
		);
		return membership;
	}

	get userId(): number {
		return this._userId;
	}

	set userId(value: number) {
		this._userId = value;
	}

	get userName(): string {
		return this._userName;
	}

	set userName(value: string) {
		this._userName = value;
	}

	get email(): string {
		return this._email;
	}

	set email(value: string) {
		this._email = value;
	}

	get role(): MembershipRole {
		return this._role;
	}

	set role(value: MembershipRole) {
		this._role = value;
	}

	get state(): MembershipState {
		return this._state;
	}

	set state(value: MembershipState) {
		this._state = value;
	}
}
