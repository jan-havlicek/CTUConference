import {MessageState} from "./message-state.enum";
import {MessageType} from "./message-type.enum";
import {Attachment} from "../attachment/attachment";

export class Message {
	private _senderId: number;
	private _senderName: string;
	private _message: string;
	private _dateSent: Date;
	private _state: MessageState;
	private _type: MessageType;
	private _attachmentList: Attachment[];

	static fromJson(jsonObject: any): Message {
		var message = new Message();
		message.senderId = jsonObject.senderId;
		message.senderName = jsonObject.senderName;
		message.message = jsonObject.message;
		message.dateSent = new Date(<string>jsonObject.dateSent);
		if(jsonObject.type === MessageType.getIdentifier(MessageType.Outgoing)) {
			message.type = MessageType.Outgoing;
			message.state = jsonObject.read ? MessageState.Read : MessageState.Unread;
		} else {
			message.type = MessageType.Incoming;
		}
		message.attachmentList = jsonObject.attachmentList
			? jsonObject.attachmentList.map(item => Attachment.fromJson(item))
			: [];
		return message;
	}

	constructor(senderId?: number, senderName?: string, message?: string, dateSent?: Date, state?: MessageState, type?: MessageType) {
		if(senderId) this._senderId = senderId;
		if(senderName) this._senderName = senderName;
		if(message) this._message = message;
		if(dateSent) this._dateSent = dateSent;
		if(state) this._state = state;
		if(type) this._type = type;
	}

	get hasAttachments() {
		return this.attachmentList && this.attachmentList.length > 0;
	}

	get isIncoming(): boolean {
		return this._type === MessageType.Incoming;
	}

	get senderId(): number {
		return this._senderId;
	}

	set senderId(value: number) {
		this._senderId = value;
	}

	get message(): string {
		return this._message;
	}

	set message(value: string) {
		this._message = value;
	}

	get dateSent(): Date {
		return this._dateSent;
	}

	set dateSent(value: Date) {
		this._dateSent = value;
	}

	get state(): MessageState {
		return this._state;
	}

	set state(value: MessageState) {
		this._state = value;
	}

	get type(): MessageType {
		return this._type;
	}

	set type(value: MessageType) {
		this._type = value;
	}

	get attachmentList(): Attachment[] {
		return this._attachmentList;
	}

	set attachmentList(value: Attachment[]) {
		this._attachmentList = value;
	}

	get senderName(): string {
		return this._senderName;
	}

	set senderName(value: string) {
		this._senderName = value;
	}
}
