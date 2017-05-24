import {SocketMessageService} from "../../communication/socket-message.service";
import {SocketMessage} from "../../communication/socket-message";
import {Message} from "./message";
import {Subject, Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {ConversationMessage} from "./conversation-message.interface";

@Injectable()
export class ChatService {

	private _messageList$: Subject<Message[]>

	private _incomingMessage$: Subject<ConversationMessage>

	constructor(
		private messageService: SocketMessageService
	) {
		this._messageList$ = <Subject<Message[]>>new Subject();
		this._incomingMessage$ = <Subject<ConversationMessage>>new Subject();
		this.messageService.subscribeToDataStream("conversation.incoming-message", (result: {data: ConversationMessage}) => {
			let conversationId = result.data.conversationId;
			let incomingMessage = Message.fromJson(result.data.message);
			this._incomingMessage$.next({conversationId: conversationId, message: incomingMessage});
		});
	}

	init(conversationId) {
		//this.listenMessagesUpdate();
		//if(conversationId) this.updateConversationMessages(conversationId);
	}

	sendMessage(message: string, conversationId: number) {
		let socketMessage = new SocketMessage("conversation.new-message", {
			message: message,
			conversationId: conversationId
		});
		this.messageService.send(socketMessage);
	}

	updateConversationMessages(conversationId) {
		let request = new SocketMessage("conversation.messages", {conversationId: conversationId});
		this.messageService.request(request).then((result) => {
			let messageList = result.data.map(item => Message.fromJson(item));
			this._messageList$.next(messageList);
		});
	}

	get messageList$() {
		return this._messageList$.asObservable();
	}

	getIncomingMessage$(conversationId: number): Observable<Message> {
		return this._incomingMessage$
			.filter(messageInfo => messageInfo.conversationId === conversationId)
			.map(messageInfo => messageInfo.message);
	}

}
