export enum CallType {
	Voice,
	Video,
	Webinar
}

export namespace CallType {

	let identifiers = [
		{type: CallType.Voice, id: "voice", label: "voice call"},
		{type: CallType.Video, id: "video", label: "video call"},
		{type: CallType.Webinar, id: "webinar", label: "webinar"}
	];

	export function getIdentifier(type: CallType): string {
		return identifiers.find(item => item.type === type).id;
	}

	export function get(identifier: string): CallType {
		return identifiers.find(item => item.id === identifier).type;
	}

	export function getLabel(type: CallType): string {
		return identifiers.find(item => item.type === type).label;
	}
}
