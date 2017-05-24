import {Component, ElementRef, OnInit, Input, Output, EventEmitter, OnDestroy} from "@angular/core";
import {CallType} from "../call-type.enum";
import {CallService} from "../call.service";
import {MediaService} from "./media.service";
import {ParticipantInfo} from "./participant-info";
import {MediaDisplayMode} from "./media-display-mode.enum";
import {Subscription} from "rxjs";

@Component({
	selector: "media-container",
	template: `<div class="media-container" (window:resize)="recalculateTileLayout()" [ngClass]="{'thumbnail-view': !isTiledLayout()}">
					<ul>
						<li *ngFor="let participant of transmittingParticipantList; let i = index" [ngClass]="{active: isActive(participant.id)}">
							<participant-media [muted]="muted" *ngIf="participant.isTransmitting" [participant]="participant"
							[incoming]="true" [callType]="callType"
							[width]="mediaWidth"
							[height]="mediaHeight"
							(speaking)="setSpeaking(participant.id)"
							(click)="setSelected(participant.id)"></participant-media>
						</li>
					</ul>
					<div *ngIf="me && me.isTransmitting" class="my-media">
						<participant-media [muted]="muted" [participant]="me" [incoming]="false" [callType]="callType"></participant-media>
					</div>
				</div>
					`
})
export class MediaContainerComponent implements OnInit, OnDestroy {

	@Input()
	displayMode: MediaDisplayMode;

	@Input()
	muted: boolean;

	@Output("participantCountChanged")
	participantCountEmitter = new EventEmitter<number>();

	participantList: ParticipantInfo[];
	me: ParticipantInfo;
	mediaWidth: number;
	mediaHeight: number;
	lastSpeakingParticipantId: number;
	selectedParticipantId: number;

	private myInfoSubscription: Subscription;
	private participantListSubscription: Subscription;

	private MEDIA_SIZE_RATIO = 4/3;

	constructor(
		private callService: CallService,
		private mediaService: MediaService,
		private el: ElementRef
	) {
		console.log("media container - construct");
		this.selectedParticipantId = null;
		this.lastSpeakingParticipantId = null;
		this.participantList = [];
	}

	ngOnInit() {
		console.log("media container - init()");
		this.participantCountEmitter.emit(0);
		this.participantListSubscription = this.mediaService.participantList$.subscribe((participantList: ParticipantInfo[]) => {
			this.participantList = participantList;
			this.participantCountEmitter.emit(this.participantList.length);
			this.recalculateTileLayout();
		});
		this.myInfoSubscription = this.mediaService.myInfo$.subscribe((myInfo: ParticipantInfo) => {
			this.me = myInfo;
		});
	}

	ngOnDestroy() {
		console.log("media container - destroy()");
		this.participantCountEmitter.emit(0);

	}

	get transmittingParticipantList(): ParticipantInfo[] {
		return this.participantList.filter(participant => participant.isTransmitting);
	}

	setSpeaking(participantId: number): void {
		this.lastSpeakingParticipantId = participantId;
	}

	setSelected(participantId: number): void {
		if(this.displayMode === MediaDisplayMode.MainSelected) {
			this.selectedParticipantId = participantId;
		}
	}

	isTiledLayout() {
		return this.displayMode === MediaDisplayMode.Tiles;
	}

	isActive(participantId): boolean {
		switch(this.displayMode) {
			case MediaDisplayMode.Tiles: return false;
			case MediaDisplayMode.MainSelected:
				if(this.selectedParticipantId === null) {
					this.selectedParticipantId = this.participantList[0].id;
				}
				return participantId === this.selectedParticipantId;
			case MediaDisplayMode.MainSpeaking:
				if(this.lastSpeakingParticipantId === null) {
					this.lastSpeakingParticipantId = this.participantList[0].id;
				}
				return participantId === this.lastSpeakingParticipantId;
		}
	}

	get callType(): CallType {
		return this.callService.callInfo.callType;
	}

	recalculateTileLayout() {
		let mediaContainerEl = this.el.nativeElement.querySelector('.media-container');
		let containerWidth = mediaContainerEl.clientWidth;
		let containerHeight = mediaContainerEl.clientHeight;
		let padding = 16; //2 * (5 border + 3 padding)
		let i = 1;
		let found = false;
		let lastWidth = 0;
		let lastHeight = 0;
		for(; i <= this.participantList.length; ++i) { // i is number of videos in row
			let rowNum = Math.ceil(this.participantList.length / i);
			var elWidth = Math.floor(containerWidth / i) - padding;
			var elHeight = Math.ceil(elWidth / this.MEDIA_SIZE_RATIO);
			let necessaryContainerHeight = (elHeight + padding) * rowNum;
			if(necessaryContainerHeight <= containerHeight) {
				found = true;
				if(lastWidth > elWidth) {
					//previous solution that fit the height is better than this
					//layout will have i-1 columns
					elWidth = lastWidth;
					elHeight = lastHeight;
				}
				break;
			} else {
				//if not fit, it will try to calculate to fit height and try
				//in next step to compare if this is better width than the
				//situation with the situation when fit the width.
				lastHeight = Math.floor(containerHeight / rowNum) - padding;
				lastWidth = Math.ceil(lastHeight * this.MEDIA_SIZE_RATIO);
			}
		}
		this.mediaWidth = elWidth;
		this.mediaHeight = found ? elHeight : containerHeight - padding;
	}
}
