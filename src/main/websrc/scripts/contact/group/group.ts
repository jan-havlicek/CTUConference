import {ConversationEntity} from "../../conversation/conversation-entity";
import {Conversation} from "../../conversation/conversation";
import {GroupType} from "./group-type.enum";
import {MembershipRole} from "./membership-role.enum";

export class Group extends ConversationEntity {

	private _groupType: GroupType;
	private _myRole: MembershipRole;
	private _isOnlyAdmin: boolean;

	static fromJson(jsonObject: any): Group {
		let group = new Group();
		group.id = jsonObject.id;
		group.name = jsonObject.name;
		group.groupType = GroupType.get(jsonObject.groupType + "");
		group.conversation = jsonObject.conversation ? Conversation.fromJson(jsonObject.conversation) : null;
		return group;
	}

	/**
	 * Audio call is available only for work team groups.
	 * Seminar groups are intended only to do only lectures and webinars.
	 * @returns {boolean}
	 */
	isVoiceCallPermitted(): boolean {
		return this._groupType == GroupType.WorkTeam;
	}

	/**
	 * Video call is available only
	 * for work team groups.
	 * Seminar groups are intended only to do only lectures and webinars.
	 * @returns {boolean}
	 */
	isVideoCallPermitted(): boolean {
		return this._groupType == GroupType.WorkTeam;
	}

	/**
	 * Webinar is available for both work team groups and seminar groups.
	 * In work group anyone can initiate the call, in seminar groups
	 * there is authorization necessary. User need to be in Admin or Lector
	 * role.
	 * @returns {boolean}
	 */
	isWebinarPermitted(): boolean {
		return this._groupType == GroupType.WorkTeam
			|| this._myRole == MembershipRole.Admin
			|| this._myRole == MembershipRole.Lector;
	}

	get groupType(): GroupType {
		return this._groupType;
	}

	set groupType(value: GroupType) {
		this._groupType = value;
	}

	get myRole(): MembershipRole {
		return this._myRole;
	}

	set myRole(value: MembershipRole) {
		this._myRole = value;
	}

	get isOnlyAdmin(): boolean {
		return this._isOnlyAdmin;
	}

	set isOnlyAdmin(value: boolean) {
		this._isOnlyAdmin = value;
	}
}
