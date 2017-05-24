export enum CallState {
	Requesting,
	Transmitting,
	None
}

export namespace CallState {

	let identifiers = [
		{state: CallState.Requesting, id: "requesting", label: "requesting"},
		{state: CallState.Transmitting, id: "transmitting", label: "transmitting"},
		{state: CallState.None, id: "none", label: "none"}
	];

	export function getIdentifier(state: CallState): string {
		return identifiers.find(item => item.state === state).id;
	}

	export function get(identifier: string): CallState {
		return identifiers.find(item => item.id === identifier).state;
	}

	export function getLabel(type: CallState): string {
		return identifiers.find(item => item.state === type).label;
	}
}
