import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";
import {GroupService} from "./group.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ContactListService} from "../contact-list/contact-list.service";
import {GroupEvent} from "./group-event";

@Component({
	selector: 'group-event',
	template: `<conversation *ngIf="groupEvent" [info]="groupEvent">
					<nav><button pButton (click)="leaveGroupEvent()">Leave</button></nav>
					<div class="overview">
						<time>Od do</time>
					</div>
				</conversation>`
})
export class GroupEventComponent implements OnInit {

	groupEvent: GroupEvent;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private authenticationService: AuthenticationService,
		private groupService: GroupService,
		private contactListService: ContactListService
	) {

	}

	ngOnInit() {
		this.route.params.forEach((params: Params) => {
			let id = +params['id'];
			// this.groupService.loadGroupEvent(id);/* .then((groupEvent) => {
			// 	this.groupEvent = groupEvent;
			// });*/
		});
	}

	leaveGroupEvent() {
		// this.groupService.leaveGroupEvent(this.groupEvent.id);/* .then(() => {
		// 	this.router.navigate(["/dashboard"]);
		// });*/
	}
}
