import {Component, OnInit} from "@angular/core";
import {Message} from "primeng/components/common/api";
import {AuthenticationService} from "../authentication/authentication.service";
import {SocketMessageService} from "../communication/socket-message.service";
import {FlashMessageService} from "./flash-message.service";
import {DeactivationReason} from "./deactivation-reason.enum";
import {AppService} from "./app.service";

@Component({
  selector: "my-app",
  template: `
<!--p-messages [value]="flashMessages"></p-messages-->
<p-growl [value]="flashMessages"></p-growl>

<header class="main-header">
	<a routerLink="/dashboard" routerLinkActive="active" class="brand">CTUConference</a>
	<nav>
		<span *ngIf="isLogged" class="logged-user">{{authIdentity?.fullName}}</span>
		<notifications *ngIf="isLogged"></notifications>
		<a *ngIf="isLogged" (click)="logout()">Log out</a>
		<a *ngIf="!isLogged" routerLink="/login">Log in</a>
	</nav>
</header>

<div class="container-fluid content">
    <contact-list *ngIf="isLogged" class="contact-list-wrapper"></contact-list>
    <div class="content-container">
		<router-outlet class="content-container-outlet"></router-outlet>
	</div>
	<call-container></call-container>
	<call-notifier *ngIf="isLogged"></call-notifier>
</div>

<div *ngIf="deactivated" class="inactive">
	<span *ngIf="deactivation === DeactivationReason.UserAccessChanged">This page is deactivated because you connected to it from another place. Please press "F5" to refresh the page.</span>
	<span *ngIf="deactivation === DeactivationReason.SocketConnectionFailed">The connection was lost. Wait for reconnection, it will take few seconds ...</span>
</div>`
})
export class AppComponent implements OnInit {

	private isLogged: boolean;

	private deactivation: DeactivationReason = null;

	DeactivationReason = DeactivationReason; // hack solution for usage of enum in template

	/**
	 * Register events of deactivation and activation. Page is deactivated in the situation,
	 * when the user switch the machines (i.e. when works in multiple devices).
	 * If the websocket connection will disappear accidentally, it will deactivate page
	 * and try to reconnect and reauthorize the socket.
	 */
	ngOnInit(): void {
		this.isLogged = false;
		//this.listenFlashMessages();
		this.authenticationService.isLogged$.subscribe(
			isLogged => {
				this.isLogged = isLogged
			});
		this.appService.deactivation$.subscribe(deactivation => {
			this.deactivation = deactivation;
		});
		//this.flashMessageService.flashMessages$.subscribe(flashMessages => this.flashMessages = flashMessages);
	}

	constructor(
		private messageService: SocketMessageService,
		private authenticationService: AuthenticationService,
		private flashMessageService: FlashMessageService,
		private appService: AppService
	) {
	}

	get deactivated() {
		return this.deactivation !== null;
	}

	get flashMessages() {
		return this.flashMessageService.flashMessages;
	}

	get authIdentity() {
		return this.authenticationService.authUser;
	}

	logout() {
		this.authenticationService.logout();
	}

	/**
	 *
	 */
	private listenFlashMessages() {
		this.messageService.subscribeToDataStream("*", (result) => {
			if(result.type == "message.error") {
				console.log("%c Error message received: ", "background: #ffdddd; color: #dd0000", result.message);
				return;
			}
			// if(result.message !== undefined && result.message !== "") {
			// 	let messageParts = result.message.split(/[:|]/);
			// 	let severity = this.getSeverity(messageParts[0]);
			// 	let summary = messageParts[1];
			// 	let detail = messageParts.length > 2 ? messageParts[2] : "";
            //
			// 	this.flashMessages.push({severity: severity, summary: summary, detail: detail});
			// 	setTimeout(() => {this.flashMessages.shift()}, 3000);
			// }
		});
	}

	/**
	 *
	 * @param severitySign
	 * @returns {any}
	 */
	private getSeverity(severitySign) {
		let severity;
		switch(severitySign) {
			case "E": severity = "error"; break;
			case "S": severity = "info"; break;
			case "W":
			default: severity = "warn";
		}
		return severity;
	}

}
