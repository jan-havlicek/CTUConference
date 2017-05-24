import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";

import {ActivatedRoute, Params} from "@angular/router";
import {FriendService} from "./friend.service";

@Component({
	selector: 'multichat',
	template: `<header>
					<h2>{{multichat.name}}</h2>
					<nav><button pButton (click)="leave">Leave</button></nav>
				</header>
				<conversation></conversation>`
})
export class MultichatComponent implements OnInit {

	constructor(
		private route: ActivatedRoute,
		private authenticationService: AuthenticationService
	) {

	}

	ngOnInit() {
		this.route.params.forEach((params: Params) => {
			let id = +params['id'];
			//this.friendService.getFriend(id);
		});
	}

	leave() {

	}
}
