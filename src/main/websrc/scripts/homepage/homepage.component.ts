
import {Component, OnInit} from "@angular/core";
import {AuthenticationService} from "../authentication/authentication.service";

type HomepageAction = "signup" | "login";

@Component({
	selector: 'homepage',
	template: `
		<section class="start-using">
			<div class="inner">
				<h2>Start using CTUConference</h2>
				<div class="hp-action">
					<section class="registration-box" *ngIf="action=='signup'">
						<a (click)="action='login'">Log in</a>
						<registration *ngIf="!isLogged"></registration>
					</section>
					<section class="login-box" *ngIf="action=='login'">
						<a (click)="action='signup'">Sign up</a>
						<h2>Log in</h2>
						<login-form></login-form>
					</section>
				</div>
			</div>
		</section>
		<section class="why-using">
			<div class="inner">
				<h2>Why to use CTUConference?</h2>
				<ul class="basic">
					<li>You can join all free webinars or create your own online seminar.</li>
					<li>You can import and use handouts in PDF or PPTX during seminar.</li>
					<li>It handles not only seminars with muted spectators, but also create group calls</li>
					<li>You can create ad-hoc call with your friends</li>
				</ul>
			</div>
		</section>`
})
export class HomepageComponent implements OnInit {

	isLogged: boolean;

	action: HomepageAction = "signup";

	constructor(
		private authenticationService: AuthenticationService
	) {
		console.log("homepage - construct");
		this.authenticationService.isLogged$.subscribe(isLogged => this.isLogged = isLogged);
	}

	ngOnInit() {
		console.log("homepage - init()");
	}
}
