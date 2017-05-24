import {Component, OnInit, Input} from '@angular/core';
import {CallService} from "./call/call.service";
import {ConversationEntity} from "./conversation-entity";
import {InfoView} from "./info-view.interface";
import {Router} from "@angular/router";
import {CallType} from "./call/call-type.enum";

@Component({
	selector: 'info',
	template: `<div class="info" [ngClass]="expanded ? 'expanded' : 'collapsed'"
					(mouseenter)="onMouseEnter()" (mouseleave)="onMouseLeave()">
				<p-panel [collapsed]="!expanded" class="collapsible">
					<p-header>
						<nav>
							<button *ngIf="isVoiceCallPermitted" [disabled]="!isGroupCallAvailable"
								pButton type="button" class="invoke-voice-call" title="Invoke voice call"
								(click)="invokeVoiceCall()"></button>
							<button *ngIf="isVideoCallPermitted" [disabled]="!isGroupCallAvailable"
								 pButton type="button" class="invoke-video-call" title="Invoke video call"
								(click)="invokeVideoCall()"></button>
							<button *ngIf="isWebinarPermitted" [disabled]="!isWebinarAvailable"
								pButton type="button" class="invoke-presentation-call" title="Invoke webinar"
								(click)="invokeWebinar()"></button>
						</nav>
						<h2>{{conversationInfo.name}}
							<button *ngIf="joinCallAvailable" pButton type="button" class="join-call"
								(click)="joinCall()" title="{{joinCallTitle}}"></button>	
						</h2>	
					</p-header>
					<ng-content></ng-content>
				</p-panel>
				<button (click)="stateChange()" class="info-toggle">{{expanded ? "Go to chat" : "Go to detail"}}</button>
			</div>`
})
export class InfoComponent implements OnInit {

	@Input("data")
	conversationInfo: ConversationEntity;

	@Input("view")
	infoView: InfoView;

	private mouseEntered: boolean;
	private expanded: boolean;
	private pinned: boolean;
	private timeoutID;

	private readonly EXPANSION_DELAY = 2000;

	constructor(
		private callService: CallService,
		private router: Router
	) {
		console.log("info - construct");
		this.expanded = false;
		this.pinned = false;
		this.mouseEntered = false;
	}

	ngOnInit() {
		console.log("info - init() "+this.conversationInfo.conversation.id);
		if(this.infoView.view == "chat") {
			this.expanded = false;
			this.pinned = false;
		} else if(this.infoView.view == "detail") {
			this.expanded = true;
			this.pinned = true;
		}
	}
/*
	get stateClass() {
		if(!this.expanded) return ["collapsed"];
		return this.pinned ? ["expanded", "pinned"] : ["expanded", "temporary"];
	}
*/
	onMouseEnter() {
		if(this.mouseEntered) return;
		this.mouseEntered = true;
		if(!this.expanded) {
			this.timeoutID = setTimeout(() => {
				this.expandTemporarily();
			}, this.EXPANSION_DELAY);
		}
	}

	onMouseLeave() {
		this.mouseEntered = false;
		clearTimeout(this.timeoutID);
		if(!this.pinned) this.collapse();
	}

	stateChange() {
		clearTimeout(this.timeoutID);
		//let newView = this.infoView.view == "chat" ? "/detail" : "";
		//this.router.navigate([this.infoView.baseUrl+newView]);
		if(this.expanded) this.collapse();
		else this.expand();
	}

	private expandTemporarily() {
		this.expanded = true;
		this.pinned = false;
	}

	private expand() {
		this.expanded = true;
		this.pinned = true;
	}

	private collapse() {
		this.expanded = false;
		this.pinned = false;
	}

	/**
	 * Invoke voice call - send notification and start calling friend or all available members of group
	 */
	invokeVoiceCall() {
		this.callService.invokeVoiceCall(this.conversationInfo.conversation.id);
	}

	/**
	 * Invoke video call - send notification and start calling friend or all available members of group
	 */
	invokeVideoCall() {
		this.callService.invokeVideoCall(this.conversationInfo.conversation.id);
	}

	/**
	 * Invoke webinar - send notification and start calling all available members of group
	 */
	invokeWebinar() {
		this.callService.invokeWebinar(this.conversationInfo.conversation.id);
	}

	/**
	 * Returns the info, whether the group call (video or voice) is currently available
	 * @returns {any}
	 */
	get isGroupCallAvailable(): boolean {
		return this.conversationInfo.isGroupCallAvailable;
	}

	/**
	 * Returns info, whether the webinar is currently available
	 * @returns {boolean}
	 */
	get isWebinarAvailable(): boolean {
		return this.conversationInfo.isWebinarAvailable;
	}

	/**
	 * Returns info, whether the voice call is permitted for this type of contact
	 * @returns {boolean}
	 */
	get isVoiceCallPermitted(): boolean {
		return this.conversationInfo.isVoiceCallPermitted();
	}

	/**
	 * Returns info, whether the video call is permitted for this type of contact
	 * @returns {boolean}
	 */
	get isVideoCallPermitted(): boolean {
		return this.conversationInfo.isVideoCallPermitted();
	}

	/**
	 * Returns info, whether the webinar is permitted for this type of contact
	 * @returns {boolean}
	 */
	get isWebinarPermitted(): boolean {
		return this.conversationInfo.isWebinarPermitted();
	}

	/**
	 * It is permitted only when the conversation is transmitting and the user is not
	 * already transmitting
	 * @returns {boolean}
	 */
	get joinCallAvailable(): boolean {
		return this.conversationInfo.isTransmitting && !this.callService.isTransmitting;
	}

	get joinCallTitle(): string {
		let title = "Join ";
		if(this.conversationInfo.transmission.callType === CallType.Voice) {
			title += "voice call";
		} else if(this.conversationInfo.transmission.callType === CallType.Video) {
			title += "video call";
		} else {
			title += "webinar";
		}
		return title;
	}

	joinCall() {
		let conversationId = this.conversationInfo.conversation.id;
		let callInfo = this.conversationInfo.transmission.callInfo;
		this.callService.acceptCall(conversationId, callInfo);
	}
}
