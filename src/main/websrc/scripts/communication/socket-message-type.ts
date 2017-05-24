export class SocketMessageType {

	private _type: string;
	private _typeParts: string[];

	constructor(type: string) {
		this._type = type;
		this._typeParts = this.split();
	}

	get type(): string {
		return this._type;
	}

	set type(type: string) {
		this._type = type;
	}

	get subType(): string {
		return this._typeParts[1];
	}

	get superType(): string {
		return this._typeParts[0];
	}

	private split(): string[] {
		var typeParts = this._type.split(/\./);
		if(typeParts.length !== 2) throw new Error("Bad message type");
		return typeParts;
	}

}
