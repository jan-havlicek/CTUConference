import {Component, OnInit, Input} from '@angular/core';
import {HandoutsService} from "./handouts.service";
import {CallService} from "../call/call.service";

@Component({
	selector: 'handouts-loader',
	template: `<div class="handouts-loader">
			<button (click)="file.click()" class="add-handouts"  [ngClass]="{persistent: persistent}" title="{{buttonTitle}}">
				<svg *ngIf="persistent"><use xlink:href="#media-container--metal-paper-clip"></use></svg>
			</button>
			<input #file type="file" accept="application/pdf" (change)="onFileChange($event)">
		</div>`
})
export class HandoutsLoaderComponent implements OnInit {

	@Input()
	persistent: boolean;

	constructor(
		private handoutsService: HandoutsService,
		private callService: CallService
	) {
		console.log("handouts loader - construct");
	};

	ngOnInit() {
		console.log("contact list - init()");
	}

	onFileChange(event) {
		var target = event.target || event.srcElement;
		let fileList: FileList = target.files;
		if(!fileList.length) return;
		let file = fileList.item(0);
		this.handoutsService.setHandouts(file, this.callService.callInfo.conversationId, this.persistent);
	}

	get buttonTitle() {
		return this.persistent ? "Add slides and store to conversation" : "Add slides"
	}
}
