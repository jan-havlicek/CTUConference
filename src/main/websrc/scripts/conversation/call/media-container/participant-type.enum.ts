export enum ParticipantType {
	CallingMember,
	Lector,
	Cameraman,
	Listener
}

export namespace ParticipantType {

	let identifiers = [
		{type: ParticipantType.CallingMember, id: "0"},
		{type: ParticipantType.Lector, id: "1"},
		{type: ParticipantType.Cameraman, id: "2"},
		{type: ParticipantType.Listener, id: "3"}
	];

	export function get(identifier: string): ParticipantType {
		return identifiers.find(item => item.id == identifier).type;
	}

	export function getIdentifier(type: ParticipantType): string {
		return identifiers.find(item => item.type == type).id;
	}

}
