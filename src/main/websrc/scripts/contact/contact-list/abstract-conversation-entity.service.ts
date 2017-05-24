import {Subject, Observable} from "rxjs";
import {ConversationEntity} from "../../conversation/conversation-entity";
import {SocketMessageService} from "../../communication/socket-message.service";
import {SocketMessage} from "../../communication/socket-message";

export abstract class AbstractConversationEntityService {

	protected _suggestions$: Subject<ConversationEntity[]>;
	protected abstract contactTypeName: string;
	protected abstract jsonConverter: (number) => ConversationEntity;

	constructor(
		protected messageService: SocketMessageService
	) {

	}

	requestContact(contactId: number): void {
		this.messageService.send(new SocketMessage(`contact.request`, {id: contactId, type: this.contactTypeName}));
	}

	contactLeave(contactId: number): void {
		this.messageService.send(new SocketMessage(`contact.leave`, {id: contactId, type: this.contactTypeName}));
	}

	/** Contact suggestion functionality **/

	loadSuggestions() {
		let message = new SocketMessage(`contact.suggest`, {type: this.contactTypeName});
		this.messageService.request(message, 'type').then((result) => {
			let suggestions = result.data.suggestions.map(item => this.jsonConverter(item));
			this._suggestions$.next(suggestions);
		});
	}

	get suggestions$(): Observable<ConversationEntity[]> {
		return this._suggestions$.asObservable();
	}
}
