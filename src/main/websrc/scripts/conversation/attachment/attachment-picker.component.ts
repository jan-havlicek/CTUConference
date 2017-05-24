import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {AuthenticationService} from "../../authentication/authentication.service";

@Component({
	selector: 'attachment-picker',
	template: `
		<input type="file">`
})
export class AttachmentPickerComponent {
	constructor(
		private router: Router,
		private authenticationService: AuthenticationService
	) {
		console.log("attachment picker - construct");
		// if(this.authenticationService.isLogged()) {
		// 	this.router.navigate(["/dashboard"]);
		// }
	};

	ngOnInit() {
		console.log("attachment picker - init()");
	}
}
