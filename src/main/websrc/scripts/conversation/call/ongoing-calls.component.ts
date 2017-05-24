import {Component, OnInit, Input, OnDestroy} from '@angular/core';

import {ActivatedRoute, Params, Router} from "@angular/router";
import {ConversationEntity} from "../conversation-entity";
import {AuthenticationService} from "../../authentication/authentication.service";
import {ContactListService} from "../../contact/contact-list/contact-list.service";
import {CallType} from "./call-type.enum";
import {CallService} from "./call.service";
import {Subscription} from "rxjs";
import {Transmission} from "./trasmission";
import {CallState} from "./call-state.enum";

@Component({
	selector: 'ongoing-calls',
	template: `<div class="ongoing-calls">
					<h2>Currently ongoing calls</h2>
					<div *ngIf="ongoingCalls.length > 0" class="ongoing-call-items-block">
						<p *ngIf="isTransmitting">You are already in call. You can join this call later.</p>
						<div class="ongoing-call-items" *ngFor="let call of ongoingCalls">
							<div class="ongoing-call">
								<h3>{{call.name}}</h3>
								<p>is {{getCallAction(call.transmission)}} {{getTransmissionType(call.transmission)}}</p>
								<button *ngIf="!isTransmitting" (click)="joinCall(call)">Join now</button>
							</div>
						</div>
					</div>
					<p *ngIf="!ongoingCalls.length" class="empty-ongoing-calls">There are currently no calls available.</p>
				</div>`
})
export class OngoingCallsComponent implements OnInit, OnDestroy {

	ongoingCalls: ConversationEntity[];

	title = "Suggested friends";

	/**
	 * Info, whether the user is currently in active call or not
	 */
	isTransmitting: boolean;

	/**
	 * Subscription - is created on init and is unsubscribed on destroy
	 */
	isTransmittingSubscription: Subscription;

	/**
	 * Subscription - is created on init and is unsubscribed on destroy
	 */
	ongoingCallsSubscription: Subscription;

	constructor(
		private contactListService: ContactListService,
		private callService: CallService
	) {
		console.log("ongoing calls - construct");
		this.ongoingCalls = [];
	}

	ngOnInit() {
		console.log("ongoing calls - init()");
		this.ongoingCallsSubscription = this.contactListService.ongoingCalls$.subscribe((calls: ConversationEntity[]) => {
			this.ongoingCalls = calls;
		});
		this.isTransmittingSubscription = this.callService.isTransmitting$.subscribe(isTransmitting =>
			this.isTransmitting = isTransmitting
		);
	}

	ngOnDestroy() {
		this.ongoingCallsSubscription.unsubscribe();
		this.isTransmittingSubscription.unsubscribe();
	}

	/**
	 * Returns string that says, what type of call it is performed
	 * @param transmission
	 * @returns {string}
	 */
	getTransmissionType(transmission: Transmission): string {
		return CallType.getLabel(transmission.callType);
	}

	/**
	 * Returns string that says, which phase is the call in
	 */
	getCallAction(transmission: Transmission): string {
		return (transmission.callState === CallState.Requesting) ? "requesting" : "performing";
	}

	/**
	 * Join the call and send this information to the server.
	 * @param conversationId
	 */
	joinCall(contact: ConversationEntity) {
		this.callService.acceptCall(contact.conversation.id, contact.transmission.callInfo);
	}
}
