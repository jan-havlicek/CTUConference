import {ContactState} from "../contact/contact-list/contact-state.enum";
import {User} from "../app/user";

/**
 * This class represents member of the conversation
 */
export class ConversationMember {
	private _id: number;
	private _avatar: string;
	private _email: string;
	private _firstName: string;
	private _lastName: string;
	/**
	 * info if the member is online/offline/transmitting
	 */
	private _state: ContactState;

	static fromJson(jsonObject): ConversationMember {
		let member = new ConversationMember();
		member.id = jsonObject.id;
		member.avatar = jsonObject.avatar;
		member.firstName = jsonObject.firstName;
		member.lastName = jsonObject.lastName;
		member.email = jsonObject.email;
		if(jsonObject.isTransmitting) {
			member.state = ContactState.Transmitting;
		} else if(jsonObject.isOnline) {
			member.state = ContactState.Online;
		} else {
			member.state = ContactState.Offline;
		}
		return member;
	}

	get fullName(): string {
		return this._firstName + " " + this._lastName;
	}

	get state(): ContactState {
		return this._state;
	}

	set state(value: ContactState) {
		this._state = value;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get avatar(): string {
		return this._avatar;
	}

	set avatar(value: string) {
		this._avatar = value;
	}

	get email(): string {
		return this._email;
	}

	set email(value: string) {
		this._email = value;
	}

	get firstName(): string {
		return this._firstName;
	}

	set firstName(value: string) {
		this._firstName = value;
	}

	get lastName(): string {
		return this._lastName;
	}

	set lastName(value: string) {
		this._lastName = value;
	}

	get isOnline() {
		return ContactState.isOnline(this._state);
	}

	get isTransmitting() {
		return ContactState.isTransmitting(this._state);
	}
}
