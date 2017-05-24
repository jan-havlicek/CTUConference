/**
 * This enumeration describes state of contact from contact list
 */
export enum ContactState {
	Online,
	Offline,
	Transmitting,
	DoNotDisturb
}

export namespace ContactState {

	let identifiers = [
		{state: ContactState.Online, id: "online"},
		{state: ContactState.Offline, id: "offline"},
		{state: ContactState.Transmitting, id: "transmitting"},
		{state: ContactState.DoNotDisturb, id: "dnd"}
	];

	export function getIdentifier(state: ContactState): string {
		return identifiers.find(item => item.state === state).id;
	}

	export function isOnline(state: ContactState): boolean {
		return state === ContactState.Online || state === ContactState.Transmitting;
	}

	export function isTransmitting(state: ContactState): boolean {
		return state === ContactState.Transmitting;
	}

	export function isDoNotDisturb(state: ContactState): boolean {
		return state === ContactState.DoNotDisturb;
	}
}
