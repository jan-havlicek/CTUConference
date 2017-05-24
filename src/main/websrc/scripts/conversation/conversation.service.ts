/*
 import {Injectable} from "@angular/core";
 import {SocketMessage} from "../communication/socket-message";
 import {Subject, Observable} from "rxjs";
 import {Conversation} from "./conversation";
 import {SocketMessageService} from "../communication/socket-message.service";

 @Injectable()
 export class ConversationService {

 private _conversation$: Subject<Conversation>;

 constructor(
 private messageService: SocketMessageService
 ) {
 this._conversation$ = <Subject<Conversation>>new Subject();
 }

 get conversation$(): Observable<Conversation> {
 return this._conversation$.asObservable();
 }

 init() {
 }

 updateConversation(conversationId) {
 let request = new SocketMessage("conversation.get", {conversationId: conversationId});
 this.messageService.request(request).then((result) => {
 let conversation = Conversation.fromJson(result.data);
 this._conversation$.next(conversation);
 });
 }
 }
 */
