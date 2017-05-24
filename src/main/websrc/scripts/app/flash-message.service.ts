import {Message} from "primeng/components/common/api";
import {BehaviorSubject, Subject, Observable} from "rxjs";

export class FlashMessageService {

	private _flashMessages: Message[];
	//private _flashMessages$: Subject<Message[]>;

	constructor(

	) {
		this._flashMessages = [];
		//this._flashMessages$ = new BehaviorSubject<Message[]>(this._flashMessages);
	}

	// get _flashMessages$(): Observable<Message[]> {
	// 	return this._flashMessages$.asObservable();
	// }

	pushMessage(message: Message) {
		this._flashMessages.push(message);
		//this._flashMessages$.next(this._flashMessages);
	}

	get flashMessages() {
		return this._flashMessages;
	}

}
