
export class FullscreenModel {

	/**
	 * Hack for typescript - disable validation for document object
	 * @type {Document}
	 */
	private _document:any = window.document;

	/**
	 * Element (plain HTML element) that should have fullscreen behavior
	 */
	private _element: any;

	/**
	 * What should happen when fullscreen is activated
	 */
	private _onEnable: () => void;

	/**
	 * What should happen when fullscreen is disabled
	 */
	private _onDisable: () => void;

	/**
	 * If the fullscreen is activated
	 * @type {boolean}
	 */
	// private _fullscreenActive = false;
	/**
	 * If the change of fullscreen state is changed manually
	 * @type {boolean}
	 */
	private _fullscreenToggled = false;

	construct() {
		console.log("Fullscreen model init");
	}

	register(element: any, onEnable: () => void, onDisable: () => void) {
		this._element = element;
		this._onEnable = onEnable;
		this._onDisable = onDisable;
		this._registerFullscreenChange();
	}

	private _registerFullscreenChange() {
		this._document.addEventListener('webkitfullscreenchange', () => this.fullscreenStateChangeHandler(), false);
		this._document.addEventListener('mozfullscreenchange', () => this.fullscreenStateChangeHandler(), false);
		this._document.addEventListener('fullscreenchange', () => this.fullscreenStateChangeHandler(), false);
		this._document.addEventListener('MSFullscreenChange', () => this.fullscreenStateChangeHandler(), false);
	}

	/**
	 * If fullscreen is toggled manually, there is not necessary another
	 * handling of display mode, but when ESC key is pressed, it is necessary
	 * to change display mode (set to maximized).
	 * @param event
	 */
	private fullscreenStateChangeHandler() {
		// if(!this._fullscreenActive) return;
		if(this._fullscreenToggled) {
			this._fullscreenToggled = false;
		} else {
			// this._fullscreenActive = false;
			this._onDisable();
		}
	}

	isFullscreenAvailable() {
		return this._document.webkitFullscreenEnabled || this._document.mozFullScreenEnabled
			|| this._document.msFullscreenEnabled;
	}

	toggleFullscreen() {
		this._fullscreenToggled = true;
		if (!this._document.fullscreenElement &&    // alternative standard method
			!this._document.mozFullScreenElement && !this._document.webkitFullscreenElement && !this._document.msFullscreenElement) {  // current working methods

			// this._fullscreenActive = true;
			this._onEnable();

			let fs = this._element && !this._element.lazy
				? this._element : this._element.element.querySelector(this._element.lazy);
			if (fs.requestFullscreen) {
				fs.requestFullscreen();
			} else if (fs.msRequestFullscreen) {
				fs.msRequestFullscreen();
			} else if (fs.mozRequestFullScreen) {
				fs.mozRequestFullScreen();
			} else if (fs.webkitRequestFullscreen) {
				fs.webkitRequestFullscreen();
			}
		} else {

			// this._fullscreenActive = false;
			this._onDisable();

			if (this._document.exitFullscreen) {
				this._document.exitFullscreen();
			} else if (this._document.msExitFullscreen) {
				this._document.msExitFullscreen();
			} else if (this._document.mozCancelFullScreen) {
				this._document.mozCancelFullScreen();
			} else if (this._document.webkitExitFullscreen) {
				this._document.webkitExitFullscreen();
			}
		}
	}

	exitFullscreen() {
		if (this._document.exitFullscreen) {
			this._document.exitFullscreen();
		} else if (this._document.msExitFullscreen) {
			this._document.msExitFullscreen();
		} else if (this._document.mozCancelFullScreen) {
			this._document.mozCancelFullScreen();
		} else if (this._document.webkitExitFullscreen) {
			this._document.webkitExitFullscreen();
		}
	}

}
