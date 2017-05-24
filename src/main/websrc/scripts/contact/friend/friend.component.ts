import {Component, OnInit, OnDestroy} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";

import {ActivatedRoute, Params, Router} from "@angular/router";
import {FriendService} from "./friend.service";
import {ContactListService} from "../contact-list/contact-list.service";
import {ContactListItem} from "../contact-list/contact-list-item";
import {Friend} from "./friend";
import {Subscription} from "rxjs";
import {InfoView} from "../../conversation/info-view.interface";

@Component({
	selector: 'friend',
	template: `<conversation *ngIf="friend" [info]="friend" [view]="infoView">
					<nav><button pButton (click)="unfriend()">Unfriend</button></nav>
				</conversation>`
})
export class FriendComponent implements OnInit, OnDestroy {

	friend: Friend;

	subscription: Subscription;

	/**
	 * Type of display info about contact. It can be displayed chat or detail
	 */
	infoView: InfoView;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private authenticationService: AuthenticationService,
		private friendService: FriendService,
		private contactListService: ContactListService
	) {
		console.log("friend - construct");
		// if(!this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/login"]);
		// }
	}

	ngOnInit() {
		console.log("friend - init()");
		this.route.params.forEach((params: Params) => {
			let friendId = +params['id'];
			let view = !params["view"] ? "" : params["view"];
			if(view !== "chat" && view !== "detail" && view !== "") this.router.navigate(['/dashboard']);
			this.infoView = {baseUrl: `/friend/${friendId}`, view: view};
			this.subscription = this.contactListService.getFriend$(friendId).subscribe((friend) => {
				this.friend = friend;
			});
		});
	}

	ngOnDestroy() {
		this.subscription && this.subscription.unsubscribe();
	}

	unfriend() {
		this.friendService.contactLeave(this.friend.id);
	}
}
