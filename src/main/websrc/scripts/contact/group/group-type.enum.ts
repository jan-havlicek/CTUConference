export enum GroupType {
	WorkTeam,
	SeminarGroup
}

export namespace GroupType {

	let identifiers = [
		{type: GroupType.WorkTeam, id: "0", label: "Work Team"},
		{type: GroupType.SeminarGroup, id: "1", label: "Seminar group"}
	];

	export function getIdentifier(type: GroupType): string {
		return identifiers.find(item => item.type == type).id;
	}

	export function get(identifier: string): GroupType {
		return identifiers.find(item => item.id == identifier).type;
	}

	export function getLabel(type: GroupType): string {
		return identifiers.find(item => item.type == type).label;
	}

	export function getOptionList() {
		let options = identifiers.map(item => {return {label: item.label, value: item.id}});
		options.unshift({label: "Select group type", value: null});
		return options;
	}
}
