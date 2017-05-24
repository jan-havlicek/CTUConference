import {Component, OnInit, Input} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";

import {Router} from "@angular/router";
import {Group} from "./group";
import {GroupService} from "./group.service";
import {FlashMessageService} from "../../app/flash-message.service";

@Component({
	selector: 'suggested-groups',
	template: `<div class="suggested-groups" *ngIf="suggestedGroups && suggestedGroups.length > 0">
					<p-carousel [headerText]="title" [value]="suggestedGroups">
						<template let-item pTemplate="body">
						<div class="contact" [ngClass]="{requested: item.requested}">
							<h3>{{item.name}}</h3>
							<button *ngIf="!item.requested" (click)="sendGroupRequest(item)">Add group</button>
							<span *ngIf="item.requested">Request sent</span>
						</div>
						</template>
					</p-carousel>
				</div>`
})
export class SuggestedGroupsComponent implements OnInit {

	suggestedGroups: Group[];
	title = "Suggested groups";

	constructor(
		private router: Router,
		private authenticationService: AuthenticationService,
		private flashMessageService: FlashMessageService,
		private groupService: GroupService
	) {
		console.log("suggested groups - construct");
		// if(!this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/login"]);
		// }
		this.groupService.suggestions$.subscribe((suggestedGroups: Group[]) => {
			this.suggestedGroups = suggestedGroups;
		});
		this.groupService.loadSuggestions();
	}

	ngOnInit() {
		console.log("suggested groups - init()");
	}

	sendGroupRequest(group: Group) {
		this.groupService.requestMembership(group.id);
		group.requested = true;
		this.flashMessageService.pushMessage({severity:'info', summary:'Group request sent', detail:`Group ${group.name} was requested`});
	}
}
