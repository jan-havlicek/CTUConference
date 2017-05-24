import { Injectable } from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import { AuthenticationService } from './authentication.service';
import {Observable} from "rxjs";

@Injectable()
export class LoggedOutGuard implements CanActivate {
    constructor(
    	private _auth: AuthenticationService,
		private _router: Router
	) {

	}

    canActivate(): boolean|Promise<boolean>|Observable<boolean> {
		if(!this._auth.authRestoreInvoked) {
			let promise = this._auth.restoreAuthentication(true);
			promise.then(isNotLogged => {
				if (!isNotLogged) {
					this._router.navigate(['/dashboard']);
				}
			});
			return promise;
			// this._auth.isNotLogged$.subscribe(
			// );
			// return this._auth.isNotLogged$;
		} else {
			return Observable.of(!this._auth.isLogged());
		}
    }
}
