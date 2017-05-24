import {Component, OnInit, Input, Inject} from '@angular/core';
import {CallService} from "../call.service";
import {CallInfo} from "../call-info";
import {CallType} from "../call-type.enum";
import {Router} from "@angular/router";
import {ContactListService} from "../../../contact/contact-list/contact-list.service";
import {FlashMessageService} from "../../../app/flash-message.service";

@Component({
	selector: 'call-notifier',
	template: `<div class="call-notifier">
				<p-dialog header="{{header}}" [(visible)]="visible" modal="modal" showEffect="fade">
					<div class="incoming" *ngIf="visible && !callInfo.isInvokedByMe">
						<h3>{{title}}</h3>
						<p>{{content}}</p>
						<footer>
							<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
								<button type="button" icon="fa-check" (click)="acceptCall()" label="Accept"></button>
								<button type="button" icon="fa-close" (click)="rejectCall()" label="Reject"></button>
							</div>
						</footer>
					</div>
					<div class="outgoing" *ngIf="visible && callInfo.isInvokedByMe">
						Waiting for other participants...
						<footer>
							<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
								<button type="button" icon="fa-close" (click)="cancelCall()" label="Cancel"></button>
							</div>
						</footer>
					</div>
				</p-dialog>	
			</div>`
})
export class CallNotifierComponent implements OnInit {
	visible: boolean = false;

	callInfo: CallInfo;

	constructor(
		private callService: CallService,
		private contactListService: ContactListService,
		private flashMessageService: FlashMessageService
	) {
		console.log("call-notifier - construct");
	}

	ngOnInit() {
		console.log("call-notifier - init()");
		this.callService.init();
		this.callService.callInvocation$.subscribe((callInfo) => {
			this.notify(callInfo);
		});
		this.callService.callTermination$.subscribe((conversationId) => {
			this.closeNotification();
			let contactName = this.contactListService.findContact(conversationId).name;
			this.flashMessageService.pushMessage({severity: "info", summary: contactName, detail: "Call was terminated."});
		});
		this.callService.callStarting$.subscribe((conversationId) => {
			this.closeNotification();
		});
	}

	ngOnChanges(changes) {
		console.log("conversation - changes of input", changes);
	}

	notify(callInfo: CallInfo) {
		this.callInfo = callInfo;
		this.visible = true;
	}

	closeNotification() {
		this.callInfo = null;
		this.visible = false;
	}

	get header(): string {
		if(!this.callInfo) return "";
		if(this.callInfo.isInvokedByMe) return "Invoking call";
		return "Incoming call";
	}

	get title(): string {
		let callType = CallType.getLabel(this.callInfo.callType);
		let callName = this.contactListService.findContact(this.callInfo.conversationId).name;
		return `${callName} requested ${callType}`;
	}

	get content(): string {
		return `${this.callInfo.callName} is calling...`;
	}

	cancelCall() {
		this.callService.cancelCall(this.callInfo.conversationId);
	}

	acceptCall() {
		this.callService.acceptCall(this.callInfo.conversationId);
	}

	rejectCall() {
		this.callService.rejectCall(this.callInfo.conversationId);
	}
}
