import {Callable} from "./callable.interface";
import {Conversation} from "./conversation";
import {ContactState} from "../contact/contact-list/contact-state.enum";
import {Transmission} from "./call/trasmission";

/**
 * Friend, group, group event, group topic
 */
export abstract class ConversationEntity implements Callable {
	private _id: number;
	private _name: string;
	private _conversation: Conversation;
	/**
	 * Local state - true if user requested contact
	 */
	private _requested: boolean;

	/**
	 * Returns info, whether there is somebody online to make the call to
	 * @returns {boolean}
	 */
	get isGroupCallAvailable(): boolean {
		return this._conversation.isGroupCallAvailable;
	}

	/**
	 * Returns info, whether there is somebody online to make the call to
	 * @returns {boolean}
	 */
	get isWebinarAvailable(): boolean {
		return this._conversation.isWebinarAvailable;
	}

	/**
	 * Returns info, that can be used for Online/Offline group state indication
	 * Online can be in the situation, that there is at leas one available member.
	 */
	get hasAvailableMembers(): boolean {
		return this._conversation.hasAvailableMembers;
	}

	/**
	 * Returns info, whether there is active call for the contact
	 * @returns {boolean}
	 */
	get isTransmitting(): boolean {
		return this._conversation.isTransmitting;
	}

	/**
	 * Get transmission for the contact (it contains call state and call type)
	 * @returns {CallType|any}
	 */
	get transmission(): Transmission {
		return this._conversation.transmission;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get name(): string {
		return this._name;
	}

	set name(value: string) {
		this._name = value;
	}

	abstract isVoiceCallPermitted(): boolean;

	abstract isVideoCallPermitted(): boolean;

	abstract isWebinarPermitted(): boolean;

	get conversation(): Conversation {
		return this._conversation;
	}

	set conversation(value: Conversation) {
		this._conversation = value;
	}

	get requested(): boolean {
		return this._requested;
	}

	set requested(value: boolean) {
		this._requested = value;
	}
}
