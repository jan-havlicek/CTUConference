import {Component, Input, ElementRef, ViewChild, Output, EventEmitter} from '@angular/core';
import {Attachment} from "./attachment";
import {FullscreenModel} from "../call/fullscreen.model";

declare var Blob: {
	prototype: Blob;
	new (): Blob;
	new (blobParts: any, options: any): Blob;
}

@Component({
	selector: 'attachment',
	template: `
		<div class="attachment" [ngClass]="{slideshow: attachment.slideshowActive}">
			<header>
				<span>{{attachment.name}}</span>
			</header>
			<div class="attachment-actions">
				<a href="/CTUConference/{{attachment.downloadUrl}}" target="_blank" download class="icon download-file" title="Download file"></a>
				<button *ngIf="attachment.isPresentation" (click)="showAsSlideshow(attachment)"
					title="Show as slideshow" class="icon show-slideshow"></button>
			</div>
			<div *ngIf="attachment.isPresentation && attachment.slideshowActive" class="attachment-handouts"
			[ngClass]="{fullscreen: isFullscreenActive}">
				<handouts [sync-type]="'no-sync'" [handouts]="attachment" #handouts
					(navHiddenChanged)="hideFullscreenButton($event)"></handouts>
				<button *ngIf="isFullscreenAvailable()"
				class="fullscreen" [ngClass]="{hidden: fullscreenButtonHidden}" (click)="toggleFullscreen()" title="Show in fullscreen">
					<svg class="icon fullscreen">
					  <use xlink:href="#media-container--fullscreen"></use>
					</svg>
				</button>		
			</div>
		</div>`
})
export class AttachmentComponent {

	@Input("model")
	attachment: Attachment;

	@Output("onSlideshow")
	slideshowEmitter = new EventEmitter();

	private fullscreenModel: FullscreenModel;

	isFullscreenActive = false;

	fullscreenButtonHidden = true;

	hideFullscreenButton(hidden: boolean) {
		this.fullscreenButtonHidden = hidden;
	}

	constructor(
		private myElement: ElementRef
	) {
		console.log("attachment - construct");
		this.fullscreenModel = new FullscreenModel();
	};

	ngOnInit() {
		console.log("attachment - init()");
		this._registerFullscreen();
	}

	private _registerFullscreen() {
		if(this.attachment.isPresentation) {
			let element = {element: this.myElement.nativeElement, lazy: ".attachment-handouts"};
			this.fullscreenModel.register(element,
				() => {
					this.isFullscreenActive = true;
				},
				() => {
					this.isFullscreenActive = false;
				}
			);
		}
	}

	toggleFullscreen() {
		this.fullscreenModel.toggleFullscreen();
	}

	isFullscreenAvailable() {
		return this.fullscreenModel.isFullscreenAvailable();
	}


	//
	// downloadAttachment() {
	// 	if(this.attachment.isDownloaded) {
	// 		this.openFile();
	// 	} else {
	// 		this.attachmentService.downloadAttachment(this.attachment)
	// 			.then(loadedAttachment => {
	// 				this.attachment = loadedAttachment;
	// 				this.openFile();
	// 			});
	// 	}
	// }

	showAsSlideshow() {
		this.attachment.slideshowActive = true;
		// this.slideshowEmitter.emit(this.myElement.nativeElement.parentElement.scrollHeight);
		// @todo emit scroll height

	}

	hideSlideshow() {
		this.attachment.slideshowActive = false;
	}

	openFile() {
		var blob = new Blob(this.attachment.toUInt8Array(), {type: this.attachment.contentType, name: this.attachment.name});
		var objectUrl = URL.createObjectURL(blob);
		window.open(objectUrl);

	}
}
