import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { Login } from './login';
import {SocketMessageService} from "../communication/socket-message.service";

@Component({
	selector: 'login-form',
	template: `<form (ngSubmit)="onSubmit()" #loginForm="ngForm">
					<div *ngIf="error" class="error-msg">Check your password</div>
					<div>
						<label for="username">Username</label>
						<input type="text" name="username" [(ngModel)]="credentials.username" required>
					</div>
					<div>
						<label for="password">Password</label>
						<input type="password" name="password" [(ngModel)]="credentials.password" required>
					</div>
					<div>
						<button type="submit" [disabled]="!loginForm.form.valid">Login</button>
					</div>
				</form>`
})
export class LoginFormComponent {
	constructor(
		private userService: AuthenticationService,
		private router: Router,
		private messageService: SocketMessageService
	) {
		console.log("login form - construct");
	};

	ngOnInit() {
		console.log("login form - init()");
	}

	credentials: Login = {username: '', password: ''};
	error = false;

	onSubmit() {
		this.userService.login(this.credentials);
		/*this.userService.login(credentials).subscribe((result) => {
		 if (result) {
		 this.router.navigate(['']);
		 error = false;
		 } else {
		 error = true;
		 }
		 });*/
		/*if(this.credentials. == 'havlicekj' && this.credentials.password == 'password') {
		 this.error = false;
		 window.localStorage.setItem('auth_token', 'token');
		 this.router.navigate(['/dashboard']);
		 } else {
		 this.error = true;
		 }*/
	}
}
