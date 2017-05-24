import { Injectable } from '@angular/core';
import {CanActivate, Router} from '@angular/router';

import { AuthenticationService } from './authentication.service';
import {Observable} from "rxjs";

@Injectable()
export class LoggedInGuard implements CanActivate {
    constructor(
    	private _auth: AuthenticationService,
		private _router: Router
	) {

	}

    canActivate(): boolean|Promise<boolean>|Observable<boolean> {
		if(!this._auth.authRestoreInvoked) {
			let promise = this._auth.restoreAuthentication();
			promise.then(isLogged => {
				if (!isLogged) {
					this._router.navigate(['/login']);
				}
			});
			return promise;
			// this._auth.isLogged$.subscribe(
			// );
			// return this._auth.isLogged$;
		} else {
			return Observable.of(this._auth.isLogged());
		}
    }
}
