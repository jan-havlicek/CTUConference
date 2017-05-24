import {CallType} from "./call-type.enum";

export class CallInfo {

	private _conversationId: number;
	private _initiatorId: number;
	private _initiatorName: string;
	private _callName: string;
	private _callType: CallType;
	private _isInvokedByMe: boolean;

	constructor(conversationId?: number, initiatorId?: number, initiatorName?: string, callName?: string, callType?: CallType, isInvokedByMe?: boolean) {
		this._conversationId = conversationId;
		this._initiatorId = initiatorId;
		this._initiatorName = initiatorName;
		this._callName = callName;
		this._callType = callType;
		this._isInvokedByMe = isInvokedByMe
	}

	static fromJson(jsonObject: any): CallInfo {
		let callInfo = new CallInfo();
		callInfo.conversationId = jsonObject.conversationId;
		callInfo.initiatorId = jsonObject.initiatorId;
		callInfo.initiatorName = jsonObject.initiatorName;
		callInfo.callName = jsonObject.callName;
		callInfo.callType = CallType.get(jsonObject.callType);
		callInfo.isInvokedByMe = false;
		return callInfo;
	}

	get conversationId(): number {
		return this._conversationId;
	}

	set conversationId(value: number) {
		this._conversationId = value;
	}

	get initiatorId(): number {
		return this._initiatorId;
	}

	set initiatorId(value: number) {
		this._initiatorId = value;
	}

	get initiatorName(): string {
		return this._initiatorName;
	}

	set initiatorName(value: string) {
		this._initiatorName = value;
	}

	get callName(): string {
		return this._callName;
	}

	set callName(value: string) {
		this._callName = value;
	}

	get callType(): CallType {
		return this._callType;
	}

	set callType(value: CallType) {
		this._callType = value;
	}

	get isInvokedByMe(): boolean {
		return this._isInvokedByMe;
	}

	set isInvokedByMe(value: boolean) {
		this._isInvokedByMe = value;
	}
}
