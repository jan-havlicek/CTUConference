import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";
import {Friend} from "../friend/friend";
import {Group} from "../group/group";
import {ContactService} from "./contact.service";
import {GroupService} from "../group/group.service";
import {FriendService} from "../friend/friend.service";
import {FlashMessageService} from "../../app/flash-message.service";

@Component({
  selector: 'contact-search',
  template: `<div class="contact-search">
			<h2>Find your friends or groups</h2>
			<input type="text" (keyup)="filterContacts($event.target.value)">
			<p-carousel *ngIf="contacts?.length > 0" [headerText]="''" [value]="contacts">
				<template let-contact pTemplate="body">
					<div class="contact" [ngClass]="{requested: contact.requested}">
						<h3>{{contact.name}}</h3>
						<button *ngIf="isGroup(contact) && !contact.requested" (click)="sendGroupRequest(contact)">Add group</button>
						<button *ngIf="isFriend(contact) && !contact.requested" (click)="sendFriendRequest(contact)">Add as friend</button>
						<span *ngIf="contact.requested">Request sent</span>
					</div>
				</template>
			</p-carousel>
			<p *ngIf="noFilterMatch" class="no-filter-match">No such contact found</p>
		</div>`
})
export class ContactSearchComponent implements OnInit {

	contacts: (Friend | Group)[];

	noFilterMatch: boolean;

    constructor(
		private authenticationService: AuthenticationService,
		private contactService: ContactService,
		private groupService: GroupService,
		private friendService: FriendService,
		private flashMessageService: FlashMessageService
	) {
		console.log("contact search - construct");
		this.contacts = [];
		this.noFilterMatch = false;
	}

	ngOnInit() {
		console.log("contact search - init()");
	}

	isGroup(contact) {
		return contact instanceof Group;
	}

	isFriend(contact) {
		return contact instanceof Friend;
	}

	/**
	 * Filter contacts - filtering is performed on every input change, but only if
	 * the filter contains at least 3 characters.
	 * @param filter
	 */
	filterContacts(filter: string) {
		if(filter.length >= 3) {
			this.contactService.filterContacts(filter).then((filteredContacts) => {
				this.contacts = filteredContacts;
				this.noFilterMatch = this.contacts.length === 0;
			});
		} else {
			this.noFilterMatch = false;
		}
	}

	sendGroupRequest(group: Group) {
		this.groupService.requestMembership(group.id);
		group.requested = true;
		this.flashMessageService.pushMessage({severity:'info', summary:'Group request sent', detail:`Group ${group.name} was requested`});
	}

	sendFriendRequest(friend: Friend) {
		this.friendService.requestContact(friend.id);
		friend.requested = true;
		this.flashMessageService.pushMessage({severity:'info', summary:'Friend request sent', detail:`${friend.name} was requested`});
	}

}
