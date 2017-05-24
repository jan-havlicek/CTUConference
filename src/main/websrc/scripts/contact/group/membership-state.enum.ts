export enum MembershipState {
	Waiting,
	Requested,
	Rejected,
	Accepted,
	Removed
}

export namespace MembershipState {

	let identifiers = [
		{type: MembershipState.Waiting, id: "0", label: "Waiting"},
		{type: MembershipState.Requested, id: "1", label: "Requested"},
		{type: MembershipState.Rejected, id: "2", label: "Rejected"},
		{type: MembershipState.Accepted, id: "3", label: "Accepted"},
		{type: MembershipState.Removed, id: "4", label: "Removed"}
	];

	export function getIdentifier(type: MembershipState): string {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: string): MembershipState {
		return identifiers.find(item => item.id === identifier).type;
	}

	export function getLabel(type: MembershipState): string {
		return identifiers.find(item => item.type === type).label;
	}
}
