import { Component } from '@angular/core';
import {AuthenticationService} from "../authentication/authentication.service";
import {Router} from "@angular/router";
import {ContactListService} from "../contact/contact-list/contact-list.service";

@Component({
  selector: 'dashboard',
  template: `<div class="simple-container dashboard">
				<p *ngIf="contactListLoaded && hasNoContacts" class="welcome-message">
				Welcome, it looks you don't have any contacts yet. Take a look at some
				suggestions or try to find you friend. It may help you to start.</p>
				<div class="dashboard-block">
					<ongoing-calls *ngIf="contactListLoaded"></ongoing-calls>
					<contact-search></contact-search>
				</div>
				<div class="dashboard-block">
					<!--suggested-friends></suggested-friends-->
					<suggested-groups></suggested-groups>
				</div>
			</div>`
})
export class DashboardComponent {

	userId: number;

	contactListLoaded = false;

	hasNoContacts = false;

    constructor(
		private router: Router,
		private authenticationService: AuthenticationService,
		private contactListService: ContactListService
	) {
		console.log('dashboard - construct');
		// if(!this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/login"]);
		// }
		this.userId = this.authenticationService.authUser.id;
		this.contactListService.contactList$.subscribe(contactList => {
			if(!!contactList && !this.contactListLoaded) {
				this.contactListLoaded = true;
				this.contactListService.isEmpty$.subscribe(empty => this.hasNoContacts = empty);
			}
		});
	}

	ngOnInit() {
		console.log('dashboard - init()');
	}
}
