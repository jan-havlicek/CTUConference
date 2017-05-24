import {MediaDisplayMode} from "./media-display-mode.enum";
export class MediaDisplayModel {
	private _mediaDisplayMode: MediaDisplayMode = MediaDisplayMode.Tiles;
	private _mediaCount = 0;

	setMediaCount(count: number) {
		this._mediaCount = count;
		if(!this.multichatLayoutEnabled) {
			this.setTilesLayout();
		}
	}

	get multichatLayoutEnabled(): boolean {
		return this._mediaCount >= 3;
	}

	get displayMode(): MediaDisplayMode {
		return this._mediaDisplayMode;
	}

	setTilesLayout() {
		this._mediaDisplayMode = MediaDisplayMode.Tiles;
	}

	setMainSpeakingLayout() {
		this._mediaDisplayMode = MediaDisplayMode.MainSpeaking;
	}

	setMainSelectedLayout() {
		this._mediaDisplayMode = MediaDisplayMode.MainSelected;
	}

	isTilesLayout() {
		return this._mediaDisplayMode === MediaDisplayMode.Tiles;
	}

	isMainSpeakingLayout() {
		return this._mediaDisplayMode === MediaDisplayMode.MainSpeaking;
	}

	isMainSelectedLayout() {
		return this._mediaDisplayMode === MediaDisplayMode.MainSelected;
	}
}
