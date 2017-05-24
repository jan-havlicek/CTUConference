import {
	Input, Component, OnInit, SimpleChange, ElementRef, AfterContentInit, AfterViewInit,
	Output, EventEmitter
} from "@angular/core";
import {ParticipantInfo} from "./participant-info";
import {MediaService} from "./media.service";
import {CallType} from "../call-type.enum";
import {SpeechDetectionService} from "./speech-detection.service";
import {ContactListService} from "../../../contact/contact-list/contact-list.service";

@Component({
	selector: "participant-media",
	template: `
		<div class="participant-media" [ngClass]="{'is-speaking': isSpeaking}">
			<audio *ngIf="isVoiceCall" muted="{{muted}}" attr.data-participant="{{participant.id}}"
				[style.width.px]="mediaWidth" [style.height.px]="mediaHeight"
				autoplay></audio>
			<video *ngIf="!isVoiceCall" [muted]="muted" attr.data-participant="{{participant.id}}"
				[style.width.px]="mediaWidth" [style.height.px]="mediaHeight"
				autoplay></video>
			<span *ngIf="incoming" class="participant-name">{{participantName}}</span>
		</div>
	`
})
export class ParticipantMediaComponent implements AfterViewInit, OnInit {

	@Input()
	participant: ParticipantInfo;

	@Input()
	callType: CallType;

	@Input()
	incoming: boolean;

	@Input()
	muted: boolean;

	@Input("height")
	mediaHeight: number;

	@Input("width")
	mediaWidth: number;

	@Output("speaking")
	speakingEmitter = new EventEmitter();

	@Output("stop-speaking")
	stopSpeakingEmitter = new EventEmitter();

	isSpeaking: boolean;

	participantName: string;

	constructor(
		private mediaService: MediaService,
		private contactListService: ContactListService,
		private speechDetectionService: SpeechDetectionService,
		private el: ElementRef
	) {
		this.isSpeaking = false;
		this.participantName = "";
		console.log("participant media - construct")
	}

	ngOnInit() {
		console.log("participant media - init() "+this.participant.id);
		this.contactListService.getMemberName$(this.participant.id).subscribe(memberName =>
			this.participantName = memberName
		);
	}

	ngAfterViewInit() {
		let mediaElement = this.getMediaElement();
		if(this.incoming) {
			this.mediaService.setParticipantStream(this.participant, mediaElement);
			this.speechDetectionService.getSpeechDetection$(this.participant.id)
				.subscribe(isSpeaking => {
					this.isSpeaking = isSpeaking;
					if(isSpeaking) this.speakingEmitter.emit();
					else this.stopSpeakingEmitter.emit();
				});
		} else {
			if(this.participant.isTransmitting) {
				this.mediaService.setMyStream(mediaElement).then((stream) => {
					this.speechDetectionService.listenSpeech(stream);
				});
			}
		}
	}

	ngOnDestroy() {
		if(this.incoming) {
			this.mediaService.closeParticipantStream(this.participant);
		} else {
			this.speechDetectionService.stopListening();
			this.mediaService.closeMyStream();
		}
	}

	get isVoiceCall() {
		return this.callType === CallType.Voice;
	}

/*
	ngOnChanges(changes: {[propertyName: string]: SimpleChange}) {
		if(changes.hasOwnProperty('participant') && this.participant
			&& changes["participant"].previousValue !== changes["participant"].currentValue) {
			let mediaElement = this.getMediaElement();
			this.mediaService.setParticipantStream(this.participant, mediaElement);
		}
	}
*/

	/**
	 * Returns audia or vide element
	 * @returns {any}
	 */
	private getMediaElement() {
		let element = this.el.nativeElement.querySelectorAll(`[data-participant="${this.participant.id}"]`);
		return element[0];
	}

}
