export enum NavbarDisplayMode {
	Vertical,
	Horizontal
}

export namespace NavbarDisplayMode {

	let identifiers = [
		{mode: NavbarDisplayMode.Vertical, id: "vertical"},
		{mode: NavbarDisplayMode.Horizontal, id: "horizontal"}
	];

	export function getIdentifier(mode: NavbarDisplayMode): string {
		return identifiers.find(item => item.mode === mode).id;
	}

}
