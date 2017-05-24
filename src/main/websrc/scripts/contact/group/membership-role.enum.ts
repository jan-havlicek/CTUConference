import {GroupType} from "./group-type.enum";
export enum MembershipRole {
	Listener,
	Organizer,
	Lector,
	Admin,
	TeamMember
}

export namespace MembershipRole {

	let identifiers = [
		{type: MembershipRole.Listener, id: "0", label: "Listener", groupTypes: [GroupType.SeminarGroup]},
		{type: MembershipRole.Organizer, id: "1", label: "Organizer", groupTypes: [GroupType.SeminarGroup]},
		{type: MembershipRole.Lector, id: "2", label: "Lector", groupTypes: [GroupType.SeminarGroup]},
		{type: MembershipRole.Admin, id: "3", label: "Admin", groupTypes: [GroupType.SeminarGroup, GroupType.WorkTeam]},
		{type: MembershipRole.TeamMember, id: "4", label: "TeamMember", groupTypes: [GroupType.WorkTeam]}
	];

	export function getIdentifier(type: MembershipRole): string {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: string): MembershipRole {
		return identifiers.find(item => item.id === identifier).type;
	}

	export function getLabel(type: MembershipRole): string {
		return identifiers.find(item => item.type === type).label;
	}

	export function getOptionList(groupType: GroupType) {
		return identifiers
			.filter(item => item.groupTypes.indexOf(groupType) !== -1)
			.map(item => { return {label: item.label, value: item.id}});
	}
}
