import {Component, OnInit, Input, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef} from '@angular/core';
import {CallService} from "../call.service";
import {Router} from "@angular/router";
import {DisplayMode} from "./display-mode.enum";
import {CallInfo} from "../call-info";
import {CallType} from "../call-type.enum";
import {AuthenticationService} from "../../../authentication/authentication.service";
import {NavbarDisplayMode} from "./navbar-display-mode.enum";
import {MediaContainerComponent} from "../media-container/media-container.component";
import {FullscreenModel} from "../fullscreen.model";
import {CallContainerLayout} from "./call-container-layout.enum";
import {HandoutsComponent} from "../../handouts/handouts.component";
import {MediaDisplayModel} from "../media-container/media-display.model";

@Component({
	selector: "call-container",
	template: `<div *ngIf="visible" class="call-container" [ngClass]="[displayModeClass, navbarDisplayModeClass, callType, isInitiatorClass]">
					<div class="call-navigation-bar">
						<div class="call-controls">
							<span class="performing-indicator">
								<svg *ngIf="callType === 'voice'" class="icon">
								  <use xlink:href="#media-container--microphone"></use>
								</svg>
								<svg *ngIf="callType === 'video'" class="icon">
								  <use xlink:href="#media-container--video-camera"></use>
								</svg>
								<svg *ngIf="callType === 'webinar'" class="icon">
								  <use xlink:href="#media-container--video-lecture"></use>
								</svg>
							</span>
							<handouts-loader *ngIf="isInitiator" [persistent]="false"></handouts-loader>
							<handouts-loader *ngIf="isInitiator" [persistent]="true"></handouts-loader>
							<button class="chat" (click)="goToChat()" title="Go to chat"></button>
							<button class="mute" [ngClass]="{muted: muted}" (click)="muted = !muted" title="Mute sound"></button>
							<span class="delimiter"></span>
							<button class="leave-call" (click)="leaveCall()" title="Leave call"></button>
							<!--button class="raise-hand" title="Raise hand">
								<svg viewBox="0 0 33 52" class="icon left-arrow">
								  <use xlink:href="#leftArrow"></use>
								</svg>
							</button>
							<button class="start-recording" title="Start recording">
								<svg viewBox="0 0 33 52" class="icon left-arrow">
								  <use xlink:href="#leftArrow"></use>
								</svg>
							</button-->
						</div>
						<div class="call-container-controls">
						
							<!-- MULTICHAT LAYOUT -->
							<button *ngIf="mediaDisplayModel.multichatLayoutEnabled" class="layout-tiles"
								(click)="mediaDisplayModel.setTilesLayout()" title="Tiles layout"
								[ngClass]="{active: mediaDisplayModel.isTilesLayout()}">
								<svg class="icon media-layout">
								  <use xlink:href="#media-container--media-layout-tiles"></use>
								</svg>
							</button>
							<button *ngIf="mediaDisplayModel.multichatLayoutEnabled" class="layout-main-selected"
								(click)="mediaDisplayModel.setMainSelectedLayout()" title="Selected stream enlarged"
								[ngClass]="{active: mediaDisplayModel.isMainSelectedLayout()}">
								<svg class="icon media-layout">
								  <use xlink:href="#media-container--media-layout-main"></use>
								</svg>
								<svg class="icon clicker">
								  <use xlink:href="#media-container--clicker"></use>
								</svg>
							</button>
							<button *ngIf="mediaDisplayModel.multichatLayoutEnabled" class="layout-main-speaking"
								(click)="mediaDisplayModel.setMainSpeakingLayout()" title="Speaking stream enlarged"
								[ngClass]="{active: mediaDisplayModel.isMainSpeakingLayout()}">
								<svg class="icon media-layout">
								  <use xlink:href="#media-container--media-layout-main"></use>
								</svg>
								<svg class="icon speaking">
								  <use xlink:href="#media-container--speaking"></use>
								</svg>
							</button>
							
							<span *ngIf="mediaDisplayModel.multichatLayoutEnabled" class="delimiter"></span>
							
							
							<!-- SLIDES LAYOUT -->
							<button *ngIf="slidesAttached" class="layout-split" (click)="layout='split'" title="Split screen"
								[ngClass]="{active: layout === 'split'}">
								<svg class="icon media">
								  <use xlink:href="#media-container--video-lecture"></use>
								</svg>
								<svg class="icon slides">
								  <use xlink:href="#media-container--presentation2"></use>
								</svg>
							</button>
							<button *ngIf="slidesAttached" class="layout-media" (click)="layout='media-first'" title="Big video"
								[ngClass]="{active: layout === 'media-first'}">
								<svg class="icon media">
								  <use xlink:href="#media-container--video-lecture"></use>
								</svg>
								<svg class="icon slides">
								  <use xlink:href="#media-container--presentation2"></use>
								</svg>
							</button>
							<button *ngIf="slidesAttached" class="layout-handouts" (click)="layout='handouts-first'" title="Big slides"
								[ngClass]="{active: layout === 'handouts-first'}">
								<svg class="icon slides">
								  <use xlink:href="#media-container--presentation2"></use>
								</svg>
								<svg class="icon media">
								  <use xlink:href="#media-container--video-lecture"></use>
								</svg>
							</button>
							
							<span *ngIf="slidesAttached" class="delimiter"></span>
							
							
							<!-- CALL CONTAINER LAYOUT -->
							<button *ngIf="displayModeClass !== 'minimized' && !isFullscreen" class="minimize" (click)="minimize()" title="Minimalize">
								<svg class="icon minimize">
								  <use xlink:href="#media-container--minimize"></use>
								</svg>
							</button>
							<button *ngIf="displayModeClass !== 'maximized' && !isFullscreen" class="maximize" (click)="maximize()" title="Maximalize">
								<svg class="icon maximize">
								  <use xlink:href="#media-container--expand"></use>
								</svg>
							</button>
							<button *ngIf="isFullscreenAvailable()" class="fullscreen" (click)="toggleFullscreen()"
								title="{{isFullscreen ? 'Exit fullscreen mode' : 'Show in fullscreen mode'}}">
								<svg class="icon fullscreen" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
								  <use xlink:href="#media-container--fullscreen"></use>
								</svg>
							</button>
							<span class="delimiter"></span>
							<button class="rotate-navbar" (click)="rotateNavbar()" title="Rotate navigation">
								<svg class="icon rotate" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
								  <use xlink:href="#media-container--rotate"></use>
								</svg>
							</button>
						</div>
					</div>
					<div class="call-layout" [ngClass]="layout">
						<media-container #mediaContainer [displayMode]="mediaDisplayModel.displayMode"
							(participantCountChanged)="mediaDisplayModel.setMediaCount($event)" [muted]="muted"></media-container>
						<div class="handouts-block">
							<handouts #handoutsContainer
								[sync-type]="isInitiator ? 'sync-master' : 'sync-slave'"
								(handoutsDisplayed)="handleHandoutsDisplayed($event)"></handouts>
						</div>
					</div>
			</div>`
})
export class CallContainerComponent implements OnInit {

	/**
	 * True when the call is performed
	 * @type {boolean}
	 */
	visible: boolean = false;

	/**
	 * Display mode of navigation bar. It can be horizontal or vertical
	 * @type {NavbarDisplayMode}
	 */
	private _navbarDisplayMode: NavbarDisplayMode = NavbarDisplayMode.Horizontal;

	/**
	 * Display mode of whole call block. It can be maximized, minimized or fullscreen
	 * @type {DisplayMode}
	 */
	private _displayMode: DisplayMode = DisplayMode.Maximized;

	mediaDisplayModel: MediaDisplayModel;
	/**
	 * Reference to child component. It is injected in order to recalculate
	 * its layout depending on navigation bar mode and on the specified layout
	 */
	@ViewChild('mediaContainer')
	mediaContainer: MediaContainerComponent;

	/**
	 * Reference to child component. It is injected in order to resize
	 * depending on the specified layout
	 */
	@ViewChild('handoutsContainer')
	handoutsContainer: HandoutsComponent;

	/**
	 * Display mode of the content of call window when slides are available.
	 * "basic" value is only present if there are any slides attached.
	 * @type {string}
	 * @private
	 */
	private _layout: CallContainerLayout = "basic";

	participantCount: number = 0;

	slidesAttached: boolean = false;

	private _fullscreenModel: FullscreenModel;

	/**
	 * The audio or video elements can be muted globally by the button
	 * @type {boolean}
	 */
	muted = false;

	constructor(
		private callService: CallService,
		private authService: AuthenticationService,
		private myElement: ElementRef,
		private changeDetector: ChangeDetectorRef
	) {
		console.log("call-container - construct");
		this._fullscreenModel = new FullscreenModel();
		this.mediaDisplayModel = new MediaDisplayModel();
	}

	ngOnInit() {
		console.log("call-container - init()");
		this.callService.callStarting$.subscribe((conversationId) => {
			this.startCall(conversationId);
		});
		this.callService.callTermination$.subscribe((conversationId) => {
			this.closeCall();
		});
		this._registerFullscreen();
	}

	private _registerFullscreen() {
		let element = {element: this.myElement.nativeElement, lazy: ".call-container"};
		this._fullscreenModel.register(element,
			() => this._displayMode = DisplayMode.Fullscreen,
			() => this._displayMode = DisplayMode.Maximized
		);
	}

	get displayModeClass(): string {
		return DisplayMode.getIdentifier(this._displayMode);
	}

	get navbarDisplayModeClass(): string {
		return NavbarDisplayMode.getIdentifier(this._navbarDisplayMode) + "-navigation";
	}


	private initDisplay() {
		this.slidesAttached = false;
		//this.mediaDisplayModel.setMediaCount(0);
		this._displayMode = DisplayMode.Maximized;
		this._layout = "basic";
		this.mediaDisplayModel.setMainSpeakingLayout();
	}

	startCall(conversationId: number) {
		this.visible = true;
		this.initDisplay();
		this.changeDetector.detectChanges();

	}

	closeCall() {
		this.visible = false;
	}

	leaveCall() {
		this.closeCall();
		this.callService.leaveCall();
	}

	minimize() {
		this._displayMode = DisplayMode.Minimized;
	}

	maximize() {
		this._displayMode = DisplayMode.Maximized;
	}

	isFullscreenAvailable(): boolean {
		return this._fullscreenModel.isFullscreenAvailable();
	}

	toggleFullscreen() {
		this._fullscreenModel.toggleFullscreen();
	}

	get callInfo(): CallInfo {
		return this.callService.callInfo;
	}

	get callType(): string {
		return CallType.getIdentifier(this.callInfo.callType);
	}

	get isInitiatorClass(): string {
		return this.isInitiator ? "is-initiator" : "";
	}

	get isInitiator(): boolean {
		return this.callInfo.initiatorId === this.authService.authUser.id;
	}

	get isFullscreen(): boolean {
		return this._displayMode === DisplayMode.Fullscreen;
	}

	rotateNavbar() {
		this._navbarDisplayMode = this._navbarDisplayMode === NavbarDisplayMode.Horizontal
			? NavbarDisplayMode.Vertical : NavbarDisplayMode.Horizontal;
		this.mediaContainer.recalculateTileLayout();
		setTimeout(() => {
			this.mediaContainer.recalculateTileLayout();
		}, 100); // für sicher - schewine
	}

	handleHandoutsDisplayed(handoutsDisplayed: boolean) {
		this.slidesAttached = handoutsDisplayed;
		if(this.slidesAttached) {
			let newLayout = <CallContainerLayout>localStorage.getItem("call-layout") || "split";
			this.layout = newLayout;
		} else {
			this.layout = "basic";
		}
	}

	get layout(): CallContainerLayout {
		return this._layout;
	}

	set layout(value: CallContainerLayout) {
		this._layout = value;
		if(value !== "basic") {
			localStorage.setItem("call-layout", value);
			setTimeout(() => {
				if(this.mediaContainer) this.mediaContainer.recalculateTileLayout();
			}, 100);
			this.handoutsContainer.resize();
		} else {
			setTimeout(() => {
				if(this.mediaContainer) this.mediaContainer.recalculateTileLayout(), 100
			});
		}
	}

	/**
	 * If user has active call and wants to see chat, it will navigate
	 * to this chat page and minimize the call window
	 */
	goToChat(): void {
		if(this.isFullscreen) {
			this._fullscreenModel.exitFullscreen();
		}
		setTimeout(() => this.minimize(), 100);
		this.callService.navigateToChat(this.callInfo.conversationId);
	}
}
