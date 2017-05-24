import {CallState} from "./call-state.enum";
import {CallType} from "./call-type.enum";
import {CallInfo} from "./call-info";

export class Transmission {
	private _callState: CallState|null;
	private _callType: CallType|null;
	private _callInfo: CallInfo|null;

	constructor(callState: CallState|null, callType: CallType|null, callInfo: CallInfo|null) {
		this._callState = callState;
		this._callType = callType;
		this._callInfo = callInfo;
	}

	get callState(): CallState|any {
		return this._callState;
	}

	set callState(value: CallState|any) {
		this._callState = value;
	}

	get callType(): CallType|any {
		return this._callType;
	}

	set callType(value: CallType|any) {
		this._callType = value;
	}

	get callInfo(): CallInfo|any {
		return this._callInfo;
	}

	set callInfo(value: CallInfo|any) {
		this._callInfo = value;
	}

	/**
	 * If it "is" transmitting, it is supposed to be in requesting or transmitting phase of call.
	 * @returns {boolean}
	 */
	isTransmitting() {
		return this._callState !== null && this._callState !== CallState.None;
	}
}
