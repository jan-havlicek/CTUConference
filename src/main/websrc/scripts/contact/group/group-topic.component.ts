import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";
import {GroupService} from "./group.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {ContactListService} from "../contact-list/contact-list.service";
import {GroupTopic} from "./group-topic";

@Component({
	selector: 'group-topic',
	template: `<conversation *ngIf="groupTopic" [info]="groupTopic">
					<h2>{{groupTopic.name}}</h2>
					<nav><button pButton (click)="leaveTopic()">Leave topic</button></nav>
					<div class="overview">
						...
					</div>
				</conversation>`
})
export class GroupTopicComponent implements OnInit {

	groupTopic: GroupTopic;

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
			// this.groupService.loadGroupTopic(id);/*.then((groupEvent) => {
			// 	this.groupTopic = groupEvent;
			// });*/
		});
	}

	leaveTopic() {
		// this.groupService.leaveGroupTopic(this.groupTopic.id);/* .then(() => {
		// 	this.router.navigate(["/dashboard"]);
		// });*/
	}
}
