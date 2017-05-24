import {Component, OnInit, Input} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";

import {ActivatedRoute, Params, Router} from "@angular/router";
import {FriendService} from "./friend.service";
import {Friend} from "./friend";
import {FlashMessageService} from "../../app/flash-message.service";

@Component({
	selector: 'suggested-friends',
	template: `<div class="suggested-friends" *ngIf="suggestedFriends && suggestedFriends.length > 0">
					<p-carousel [headerText]="title" [value]="suggestedFriends">
						<template let-item  pTemplate="body">
							<div class="contact" [ngClass]="{requested: item.requested}">
								<h3>{{item.name}}</h3>
								<button *ngIf="!item.requested" (click)="sendFriendRequest(item)">Add as friend</button>
								<span *ngIf="item.requested">Request sent</span>
							</div>
						</template>
					</p-carousel>
				</div>`
})
export class SuggestedFriendsComponent implements OnInit {

	@Input()
	hasFriends: boolean = true;

	suggestedFriends: Friend[];

	title = "Suggested friends";

	constructor(
		private router: Router,
		private authenticationService: AuthenticationService,
		private flashMessageService: FlashMessageService,
		private friendService: FriendService
	) {
		console.log("friend - construct");
		// if(!this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/login"]);
		// }
		this.friendService.suggestions$.subscribe((suggestedFriends: Friend[]) => {
			this.suggestedFriends = suggestedFriends;
		});
		this.friendService.loadSuggestions();
	}

	ngOnInit() {
		console.log("suggested friends - init()");
	}

	sendFriendRequest(friend: Friend) {
		this.friendService.requestContact(friend.id);
		friend.requested = true;
		this.flashMessageService.pushMessage({severity:'info', summary:'Friend request sent', detail:`${friend.name} was requested`});
	}
}
