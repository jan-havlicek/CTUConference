export enum MessageType {
	Incoming,
	Outgoing
}

export namespace MessageType {

	let identifiers = [
		{type: MessageType.Incoming, id: "incoming"},
		{type: MessageType.Outgoing, id: "outgoing"}
	];

	export function getIdentifier(type: MessageType): string {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: string): MessageType {
		return identifiers.find(item => item.id === identifier).type;
	}

}
