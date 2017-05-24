import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {validateEqual} from "../app/form/validators/equal.validator";
import {Registration} from "./registration";
import {RegistrationService} from "./registration.service";
import {AuthenticationService} from "../authentication/authentication.service";

@Component({
  selector: 'registration',
  template: `
	<div class="registration">
		<h2>Sign up</h2>
		<form novalidate (ngSubmit)="register()"
			[formGroup]="regForm">
			<div>
				<label for="email">Email</label>
				<input type="email" formControlName="email" class="email" required>
				<div *ngIf="regForm.controls.email.dirty && !regForm.controls.email.valid">
				   <p *ngIf="regForm.controls.email.errors.required" class="error-msg">
					  Email is required
				   </p>
				   <p *ngIf="regForm.controls.email.errors.pattern" class="error-msg">
					  Email format is not valid
				   </p>
				</div>
			</div>
			<div>
				<label for="firstName">Name</label>
				<input type="text" formControlName="firstName" class="first-name" required>
				<input type="text" formControlName="lastName" class="last-name" required>
				<div *ngIf="regForm.controls.firstName.dirty && !regForm.controls.firstName.valid">
				   <p *ngIf="regForm.controls.firstName.errors.required" class="error-msg">
					  First name is required
				   </p>
				</div>
				<div *ngIf="regForm.controls.lastName.dirty && !regForm.controls.lastName.valid">
				   <p *ngIf="regForm.controls.lastName.errors.required" class="error-msg">
					  Last name is required
				   </p>
				</div>
			</div>
			<div>
				<label for="password">Password</label>
				<input type="password" formControlName="password">
			</div>
			<div *ngIf="regForm.controls.password.dirty && !regForm.controls.password.valid">
			   <p *ngIf="regForm.controls.password.errors.minLength || regForm.controls.password.errors.required || regForm.controls.password.errors.pattern" class="error-msg">
				  Password should have 6 chars.
			   </p>
			</div>
			<div>
				<label for="passwordConfirm">Confirmation</label>
				<input type="password" formControlName="passwordConfirm">
			</div>
			<div *ngIf="regForm.controls.passwordConfirm.dirty && !regForm.controls.passwordConfirm.valid">
			   <p *ngIf="regForm.controls.passwordConfirm.errors.validateEqual" class="error-msg">
				  Password mismatch
			   </p>
			</div>
			<div>
				<button type="submit" [disabled]="!regForm.valid">Sign up</button>,
			</div>
		</form>
	</div>`
})
export class RegistrationComponent implements OnInit {

	regForm: FormGroup;

	registration: Registration;

	constructor(
		private formBuilder: FormBuilder,
		private authService: AuthenticationService
		//private registrationService: RegistrationService
	) {
		console.log("registration - construct");
	}

	ngOnInit() {
		console.log("registration - init()");
		this.regForm = this.formBuilder.group({
			email: ['', [
				Validators.required,
				Validators.pattern(".+@.+")
			]],
			firstName: ['', Validators.required],
			lastName: ['', Validators.required],
			password: ['', [
				Validators.required,
				Validators.pattern("^.{6,}$"),
				Validators.minLength(6)
			]],
			passwordConfirm: ['', [
				validateEqual('password')
			]],
			avatar: ['']
		});
	}

	register() {
		let registration = <Registration> this.regForm.getRawValue();
		this.authService.register(registration);
		//this.registrationService.register(registration);
	}
}
