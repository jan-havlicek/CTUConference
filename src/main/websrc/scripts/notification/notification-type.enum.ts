export enum NotificationType {
	Info
}

export namespace NotificationType {

	let identifiers = [
		{type: NotificationType.Info, id: "voice", label: "voice call"}
	];

	export function getIdentifier(type: NotificationType): string {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: string): NotificationType {
		return identifiers.find(item => item.id === identifier).type;
	}

	export function getLabel(type: NotificationType): string {
		return identifiers.find(item => item.type === type).label;
	}
}
