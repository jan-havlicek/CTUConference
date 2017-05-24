export enum DisplayMode {
	Minimized,
	Maximized,
	Fullscreen
}

export namespace DisplayMode {

	let identifiers = [
		{mode: DisplayMode.Minimized, id: "minimized"},
		{mode: DisplayMode.Maximized, id: "maximized"},
		{mode: DisplayMode.Fullscreen, id: "fullscreen"}
	];

	export function getIdentifier(mode: DisplayMode): string {
		return identifiers.find(item => item.mode === mode).id;
	}

}
