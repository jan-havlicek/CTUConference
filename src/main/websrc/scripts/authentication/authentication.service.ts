// user.service.ts
import { Injectable } from '@angular/core';
import { SocketMessage } from '../communication/socket-message';
import { Login } from './login';
import {User} from "../app/user";
import {SocketMessageService} from "../communication/socket-message.service";
import {ApiService} from "../utils/api.service";
import {Router} from "@angular/router";
import {Observable, Subject, BehaviorSubject} from "rxjs";
import Result = jasmine.Result;
import {Registration} from "../registration/registration";
import {FlashMessageService} from "../app/flash-message.service";
import {DeactivationReason} from "../app/deactivation-reason.enum";
import {AppService} from "../app/app.service";

@Injectable()
export class AuthenticationService {

	private API_PATH = '/api/auth';

	private _authUser$: Subject<User>;
	private _authUser: User;
	private _authRestoreInvoked: boolean;

    constructor(
    	private messageService: SocketMessageService,
		private router: Router,
		private apiService: ApiService,
		private flashMessageService: FlashMessageService,
		private appService: AppService
	) {
		this._authUser = null;
		this._authUser$ = <BehaviorSubject<User>>new BehaviorSubject(null);
		this._authRestoreInvoked = false;
		this.messageService.subscribeToDataStream("authentication.deactivate-socket", result  =>
			this.appService.deactivate(DeactivationReason.UserAccessChanged)
		);
		// when the connection failure appears, it is necessary to reauthorize socket (after the
		// connection is open again)
		this.messageService.connectionFailure$.subscribe(isFailure => {
			if(isFailure) {
				if(this.messageService.isConnectionOpen) this._restoreClosedConnection();
				else this.messageService.onOpenAction = this._restoreClosedConnection.bind(this);
			}
		});
    }

    private _restoreClosedConnection() {
		this.restoreAuthentication().then(restored => {
			if(restored) {
				this.flashMessageService.pushMessage({severity: "info", summary: "Reconnected", detail: "Connection is restored"});
			} else {
				this.router.navigate(['/login']);
			}
		});
	}

	/**
	 * Authenticate the user via HTTP protocol. Response of authentication
	 * should contain one-time token, that is then used for web socket authorization.
	 * @param credentials
	 */
	login(credentials: Login) {
		this._authenticate(credentials).subscribe(response => {
			this._authorizeWebSocket(response.token).then(() => {
				this._setAuthIdentity(User.fromJson(response));
				this.router.navigate(["/dashboard"]);
			});
		}, err => {
			this.flashMessageService.pushMessage({severity: "error", summary: "Login failed", "detail": "Log in was not successful. Check your credentials or try it later."})
		});
    }

    logout() {
        this._unauthorizeWebSocket();
		this._requestLogout().subscribe(response => {
			this.router.navigate(["/login"]);
			this._setAuthIdentity(null);
		});
    }

    register(registration: Registration) {
		this._register(registration).subscribe(response => {
			this._authorizeWebSocket(response.token).then(() => {
				this._setAuthIdentity(User.fromJson(response));
				this.router.navigate(["/dashboard"]);
			});
		}, err => {
			this.flashMessageService.pushMessage({severity: "error", summary: "Registration failed", "detail": "Registration was not successful. Check your information or try it later."})
		});
	}

	get authUser(): User {
		return this._authUser;
	}

	private _setAuthIdentity(authIdentity: User) {
		this._authUser = authIdentity;
		this._authUser$.next(this._authUser);
	}

	get authUser$(): Observable<User> {
		return this._authUser$.asObservable();
	}

	get authRestoreInvoked(): boolean {
		return this._authRestoreInvoked;
	}

	get isLogged$(): Observable<boolean> {
		return this._authUser$.map(item => item !== null);
	}

    isLogged() {
		return this._authUser !== null
	}

	/**
	 * Send HTTP GET request to login
	 * @private
	 */
    private _requestLogout(): Observable<any> {
		let url = this.API_PATH + "/logout";
		return this.apiService.getJson(url);

	}

	/**
	 * Tries to restore socket authorization
	 * Requests new token that is provided only if the user is logged in.
	 * When token is obtained, it is then used to authorize the websocket
	 */
	restoreAuthentication(promiseNotLogged = false): Promise<boolean> {
		return new Promise<boolean>((resolve, reject) => {
			this._authRestoreInvoked = true;
			this._requestAuthorizationToken().then(token => {
				this._authorizeWebSocket(token).then((response) => {
					this._setAuthIdentity(response);
					this.appService.activate();
					resolve(!promiseNotLogged ? true : false);
				}).catch(err => {
					this._setAuthIdentity(null);
					this.appService.activate();
					resolve(!promiseNotLogged ? false : true);
				});
			}).catch(err => {
				this._setAuthIdentity(null);
				this.appService.activate();
				resolve(!promiseNotLogged ? false : true);
			});
		});
	}

	/**
	 * @param credentials
	 * @returns {Observable<any>}
	 * @private
	 */
	private _authenticate(credentials: Login): Observable<any> {
		let url = this.API_PATH + "/login";
		let data = {
			"login": credentials.username,
			"password": credentials.password
		}
		return this.apiService.postJson(url, data);
	}

	/**
	 * @param credentials
	 * @returns {Observable<any>}
	 * @private
	 */
	private _register(credentials: Registration): Observable<any> {
		let url = this.API_PATH + "/register";
		let data = {
			"email": credentials.email,
			"firstName": credentials.firstName,
			"lastName": credentials.lastName,
			"password": credentials.password
		}
		return this.apiService.postJson(url, data);
	}

	/**
	 *
	 * @param token
	 * @returns {Promise<any>}
	 */
	private _authorizeWebSocket(authToken: string): Promise<any> {
		let message = new SocketMessage("authentication.authorize-socket", {token: authToken});
		return new Promise<any>((resolve, reject) => {
			this.messageService.request(message).then(result => {
				if(result.data.state === true) {
					resolve(User.fromJson(result.data.user));
				} else {
					reject();
				}
			});
		});
	}

	private _unauthorizeWebSocket(): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			this.messageService.request("authentication.unauthorize-socket").then(result => {
				if(result.data.state === true) {
					resolve();
				} else {
					reject();
				}
			});
		});
	}

	private _requestAuthorizationToken(): Promise<string> {
		return new Promise((resolve, reject) => {
			let url = this.API_PATH + "/token";
			this.apiService.getJson(url).subscribe(
				response => {
					if(response.token) {
						resolve(response.token);
					} else {
						reject();
					}
				},
				error => {
					reject();
				})
		});
	}
}
