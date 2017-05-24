
export enum RelationshipAction {
	Requested,
	Accepted,
	Rejected,
	Removed,
	ChangedPermissions
}

export namespace RelationshipAction {

	let identifiers = [
		{type: RelationshipAction.Requested, id: 0, label: "requested"},
		{type: RelationshipAction.Accepted, id: 1, label: "accepted"},
		{type: RelationshipAction.Rejected, id: 2, label: "rejected"},
		{type: RelationshipAction.Removed, id: 3, label: "removed"},
		{type: RelationshipAction.ChangedPermissions, id: 4, label: "changed permissions"}
	];

	export function getIdentifier(type: RelationshipAction): number {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: number): RelationshipAction {
		return identifiers.find(item => item.id === identifier).type;
	}

	export function getLabel(type: RelationshipAction): string {
		return identifiers.find(item => item.type === type).label;
	}
}
