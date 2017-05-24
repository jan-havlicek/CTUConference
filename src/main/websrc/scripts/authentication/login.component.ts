import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Component({
    selector: 'login',
    template: `
		<section class="login">
			<div class="inner">
				<h2>Log in</h2>
				<login-form></login-form>
			</div>
		</section>`
})
export class LoginComponent {
    constructor(
    	private router: Router,
		private authenticationService: AuthenticationService
	) {
		console.log("login - construct");
		// this.authenticationService.isLogged$.subscribe(isLogged => {
		// 	if(!isLogged) this.router.navigate(["/dashboard"]);
		// });
	};

	ngOnInit() {
		console.log("login - init()");
	}
}
