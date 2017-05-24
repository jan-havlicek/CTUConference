export enum MediaDisplayMode {
	Tiles,
	MainSelected,
	MainSpeaking
}

export namespace NavbarDisplayMode {

	let identifiers = [
		{mode: MediaDisplayMode.Tiles, id: "tiles"},
		{mode: MediaDisplayMode.MainSelected, id: "main-selected"},
		{mode: MediaDisplayMode.MainSpeaking, id: "main-speaking"}
	];

	export function getIdentifier(mode: MediaDisplayMode): string {
		return identifiers.find(item => item.mode === mode).id;
	}

}
