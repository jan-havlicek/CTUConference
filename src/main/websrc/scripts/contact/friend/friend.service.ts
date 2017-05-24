import {Injectable} from "@angular/core";
import {Subject, Observable} from "rxjs";
import {Friend} from "./friend";
import {ContactListService} from "../contact-list/contact-list.service";
import {SocketMessageService} from "../../communication/socket-message.service";
import {SocketMessage} from "../../communication/socket-message";

@Injectable()
export class FriendService {
	protected _suggestions$: Subject<Friend[]>;

	constructor(
		private messageService: SocketMessageService,
	) {
		this._suggestions$ = <Subject<Friend[]>>new Subject();
	}

	requestContact(userId: number): void {
		this.messageService.send(new SocketMessage(`friendship.request`, {userId: userId}));
	}

	acceptContact(userId: number): void {
		this.messageService.send(new SocketMessage(`friendship.accept`, {userId: userId}));
	}

	rejectContact(userId: number): void {
		this.messageService.send(new SocketMessage(`friendship.reject`, {userId: userId}));
	}

	contactLeave(userId: number): void {
		this.messageService.send(new SocketMessage(`friendship.leave`, {userId: userId}));
	}

	/** Contact suggestion functionality **/

	loadSuggestions() {
		let message = new SocketMessage(`friendship.suggest`, {});
		this.messageService.request(message).then((result) => {
			let suggestions = result.data.suggestions.map(item => Friend.fromJson(item));
			this._suggestions$.next(suggestions);
		});
	}

	get suggestions$(): Observable<Friend[]> {
		return this._suggestions$.asObservable();
	}

}
