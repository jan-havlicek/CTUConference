import {SocketMessageType} from "./socket-message-type";
/**
 * This represents message comming from websockets.
 * It is designed to be in JSON format cotaining this parameters:
 *  - type: in format "supertype.subtype", it is required
 *  - message: this is optional parameter. It is used to fill flash messages.
 *  - data: this is required parameter and should contain data in JSON format.
 *
 */
export class SocketMessage {
	public _type: SocketMessageType;
	public message: string;
	public data: any;

	constructor(type: string, data?: any);
	constructor(messageObj: any);

	constructor(typeOrMessageObj: string | any, data?: any) {
		if(typeof typeOrMessageObj === "string") {
			let type = typeOrMessageObj;
			this._type = new SocketMessageType(type);
			this.data = data || {};
			this.message = "";
		} else {
			let messageObj = typeOrMessageObj;
			this.message = messageObj.message || "";
			if(messageObj.data) {
				this.data = messageObj.data;
			} else {
				throw new Error("Message has no data");
			}
			if(messageObj.type) {
				this._type = new SocketMessageType(messageObj.type);
			} else {
				throw new Error("Message has no type");
			}
		}
	}

	get type(): string {
		return this._type.type;
	}

	get subType(): string {
		return this._type.subType;
	}

	get superType(): string {
		return this._type.superType;
	}

	/**
	 * Returns JSON containing message type, data and when message
	 * is filled then it will contain also message parameter.
	 * @returns {{type: SocketMessageType, message: string, data: any}}
	 */
	toJSON() {
		return {
			type: this._type.type,
			message: this.message === "" ? undefined : this.message,
			data: this.data
		};
	}
}
