
import {ConversationMember} from "./conversation-member";
import {CallType} from "./call/call-type.enum";
import {ContactState} from "../contact/contact-list/contact-state.enum";
import {CallState} from "./call/call-state.enum";
import {Transmission} from "./call/trasmission";
import {CallInfo} from "./call/call-info";
export class Conversation {
	private _id: number;
	private _memberList: ConversationMember[];
	private _transmission: Transmission;

	static fromJson(jsonObject: any): Conversation {
		let conversation = new Conversation();
		conversation.id = jsonObject.id;
		//conversation.name = jsonObject.name;
		conversation.memberList = jsonObject.participantList
			? jsonObject.participantList.map(item => ConversationMember.fromJson(item))
			: [];
		conversation.transmission = new Transmission(
			jsonObject.callState ?  CallState.get(jsonObject.callState) : null,
			jsonObject.callType ?  CallType.get(jsonObject.callType) : null,
			jsonObject.callInfo ?  CallInfo.fromJson(jsonObject.callInfo) : null
		);
		return conversation;
	}

	/**
	 * Returns info, whether there is somebody online to make the group call to
	 * (someone who is also not transmitting)
	 * @returns {boolean}
	 */
	get isGroupCallAvailable(): boolean {
		return this.hasAvailableMembers;
	}

	get hasAvailableMembers(): boolean {
		return this.memberList
				.filter(item => item.state === ContactState.Online).length > 0
			&& !this.isTransmitting;
	}

	/**
	 * Returns info, whether webinar is available
	 * @returns {boolean}
	 */
	get isWebinarAvailable(): boolean {
		return !this.isTransmitting;
	}

	get isTransmitting(): boolean {
		return this.transmission.isTransmitting();
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get memberList(): ConversationMember[] {
		return this._memberList;
	}

	set memberList(value: ConversationMember[]) {
		this._memberList = value;
	}

	getMember(memberId: number): ConversationMember {
		return this._memberList.find(member => member.id === memberId);
	}

	get transmission(): Transmission {
		return this._transmission;
	}

	set transmission(value: Transmission) {
		this._transmission = value;
	}
}
