import {Injectable} from "@angular/core";
import {DeactivationReason} from "./deactivation-reason.enum";
import {Subject, BehaviorSubject, Observable} from "rxjs";
import {SocketMessageService} from "../communication/socket-message.service";

@Injectable()
export class AppService {
	private _deactivation$: Subject<DeactivationReason>;

	constructor(
		private messageService: SocketMessageService
	) {
		this._deactivation$ = <Subject<DeactivationReason>>new BehaviorSubject(null);
		this.messageService.connectionFailure$.subscribe(isFailure => {
			if(isFailure) this._deactivation$.next(DeactivationReason.SocketConnectionFailed);
		});
	}

	deactivate(reason: DeactivationReason) {
		this._deactivation$.next(reason);
	}

	activate() {
		this._deactivation$.next(null);
	}

	get deactivation$(): Observable<DeactivationReason> {
		return this._deactivation$.asObservable();
	}
}
