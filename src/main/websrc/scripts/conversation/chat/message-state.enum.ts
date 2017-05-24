export enum MessageState {
	Sending,
	UnableToSend,
	Unread,
	Read
}

export namespace MessageState {

	let identifiers = [
		{state: MessageState.Sending, id: "sending"},
		{state: MessageState.UnableToSend, id: "unable-to-read"},
		{state: MessageState.Unread, id: "unread"},
		{state: MessageState.Read, id: "read"}
	];

	export function getIdentifier(state: MessageState): string {
		return identifiers.find(item => item.state === state).id;
	}

	export function get(identifier: string): MessageState {
		return identifiers.find(item => item.id === identifier).state;
	}
}

