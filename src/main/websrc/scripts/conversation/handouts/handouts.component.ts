import {Component, Input, Output, EventEmitter, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../../authentication/authentication.service";
import {Attachment} from "../attachment/attachment";
import {HandoutsService} from "./handouts.service";
import {HandoutsSyncType} from "./handouts-sync-type";

@Component({
	selector: 'handouts',
	template: `<div *ngIf="handouts" class="handouts-container" (mousemove)="showNavigation()">
		<div class="handouts" (window:resize)="resize()">
		<pdf-viewer [src]="pdfSource" 
              [(page)]="myCurrentSlide" 
              [original-size]="false"
              [show-all]="false"
              [zoom]="1"
              [render-text]="renderText"
              style="display: block;"></pdf-viewer>
		</div>
		<div class="handouts-navigation" [ngClass]="{hidden: navigationHidden}">
			<button *ngIf="syncType == 'sync-slave'" class="sync"
				[ngClass]="{missed: newUpdateMissed}" (click)="synchronize()"
				[disabled]="isSynchronized()" title="{{syncButtonTitle}}">
				<svg cla ss="icon sync">
				  <use xlink:href="#media-container--sync"></use>
				</svg>
			</button>
			<!--span *ngIf="syncType == 'sync-slave' && isSynchronized()" class="sync"></span-->
			<button [disabled]="isOnBeginning()" class="prev" (click)="goBack()"></button>
			<button [disabled]="isOnEnd()" class="next" (click)="goNext()"></button>
			<button *ngIf="downloadEnabled" (click)="download()">Download slides</button>
		</div>
	</div>`
})
export class HandoutsComponent implements OnInit, OnDestroy {

	@Input('sync-type')
	syncType: HandoutsSyncType;

	@Input('download-enabled')
	downloadEnabled = false;

	/**
	 * It is filled with an object when it is an attachment
	 * in the chat. When used in call container, there is
	 * default null set and
	 * @type {Attachment}
	 */
	@Input()
	handouts: Attachment = null;

	@Output() //true if hidden, false if displayed
	navHiddenChanged = new EventEmitter();

	@Output() //true if displayed, false if not
	handoutsDisplayed = new EventEmitter();

	renderText = false;
	pdfSource = null;

	resize() {
		this.renderText = true;
		setTimeout(() => this.renderText = false, 50);
	}

	slidesCount: number;
	myCurrentSlide: number = 1;
	globalCurrentSlide: number;
	newUpdateMissed: boolean = false;
	navigationHidden: boolean = true;
	private _navHideTimeoutID;

	constructor(
		private router: Router,
		private authenticationService: AuthenticationService,
		private handoutsService: HandoutsService
	) {
		console.log('handouts - construct');
		// if(!this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/login"]);
		// }
	}

	ngOnInit() {
		console.log('handouts - init()');
		if(this.handouts !== null && this.handouts.downloadUrl) {
			this.pdfSource = "/CTUConference" + this.handouts.downloadUrl;
			this.handoutsDisplayed.emit(true);
		}

		if(this.syncType === "sync-slave") {
			this.handoutsService.handoutsUpdates$.subscribe(globalCurrentSlide => {
				if(this.isSynchronized()) {
					this.globalCurrentSlide = globalCurrentSlide;
					this.myCurrentSlide = globalCurrentSlide;
				} else {
					this.globalCurrentSlide = globalCurrentSlide;
					this.newUpdateMissed = this.isSynchronized() ? false : true;
				}
			})
		}
		if(this.handouts === null) {
			this.handoutsService.handouts$.subscribe((handouts: Attachment) => {
				this.handouts = handouts;
				this.pdfSource = handouts.downloadUrl || handouts.data;
				this.globalCurrentSlide = handouts.initialPage;
				this.handoutsDisplayed.emit(true);
				this.myCurrentSlide = 1; //@todo request current handouts page
			});
		}
	}

	ngOnDestroy() {
		this.handoutsDisplayed.emit(false);
	}

	isOnBeginning() {
		return this.myCurrentSlide === 1;
	}

	get syncButtonTitle() {
		return `Synchronize${this.newUpdateMissed ? " -  incoming new page change" : ""}`;
	}

	isOnEnd() {
		return this.myCurrentSlide === this.slidesCount;
	}

	goNext() {
		let newPage = this.myCurrentSlide + 1;
		this.goToPage(newPage);
		if(this.syncType == "sync-master") {
			this.handoutsService.synchronizePage(newPage);
		}
	}

	goBack() {
		let newPage = this.myCurrentSlide - 1;
		this.goToPage(newPage);
		if(this.syncType == "sync-master") {
			this.handoutsService.synchronizePage(newPage);
		}
	}

	synchronize() {
		this.myCurrentSlide = this.globalCurrentSlide;
		this.newUpdateMissed = false;
	}

	isSynchronized() {
		return this.myCurrentSlide === this.globalCurrentSlide;
	}

	private goToPage(page) {
		this.myCurrentSlide = page;
		if(this.isSynchronized()) {
			this.newUpdateMissed = false;
		}
	}

	download() {
		//@todo download handouts
	}

	showNavigation() {
		this.navigationHidden = false;
		this.navHiddenChanged.emit(false);
		if(this._navHideTimeoutID) {
			clearTimeout(this._navHideTimeoutID);
		}
		this._navHideTimeoutID = setTimeout(() => {
			this.hideNavigation();
		}, 1500);
	}

	hideNavigation() {
		this.navigationHidden = true;
		this.navHiddenChanged.emit(true);
	}
}
