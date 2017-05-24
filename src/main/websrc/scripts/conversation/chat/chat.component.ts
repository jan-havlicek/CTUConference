import {Component, OnInit, Input, SimpleChange, ViewChild, ElementRef, OnDestroy} from '@angular/core';
import {Message} from "./message";
import {Conversation} from "../conversation";
import {ChatService} from "./chat.service";
import {MessageState} from "./message-state.enum";
import {MessageType} from "./message-type.enum";
import {Subscription} from "rxjs";

@Component({
  selector: 'chat',
  template: `<div class="chat" #chatContainer>
			<ul *ngIf="messageList?.length > 0">
				<li *ngFor="let message of messageList" class="chat-message"
				[ngClass]="getTypeClass(message.type)">
					<span *ngIf="message.isIncoming" rel="author" class="message-author">{{getMessageAuthor(message)}}</span>
					<time datetime="message.dateSent">{{message.dateSent | date:'short'}}</time>
					<p class="message-content">{{message.message}}</p>
					<ul *ngIf="message.attachmentList && message.attachmentList.length > 0" class="attachment-list">
						<li *ngFor="let attachment of message.attachmentList" [ngClass]="{slideshow: attachment.slideshowActive}">
							<attachment [model]="attachment" (onSlideshow)="setScrollTarget($event)"></attachment>
						</li>
					</ul>
					<span *ngIf="!message.isIncoming" [ngClass]="getStateClass(message.state)"></span>
				</li>
			</ul>
			<p class="empty-chat" *ngIf="!messageList?.length">This conversation is still empty</p>
			<div class="message-input">
				<form (ngSubmit)="sendMessage()" #messageForm="ngForm">
					<input type="text" name="newMessage" [(ngModel)]="newMessage" required>
					<button type="submit" [disabled]="!messageForm.form.valid || !conversation">Odeslat</button>
				</form>
			</div>
			</div>`
})
export class ChatComponent implements OnInit, OnDestroy {

	/**
	 * Conversation - it is taken from contact list model.
	 */
	@Input()
	conversation: Conversation;

	/**
	 * Message list of all participants of the conversation
	 */
	messageList: Message[];

	/**
	 * Input message - message obtained from text input
	 */
	newMessage: string;

	/**
	 * sign for future scroll
	 * @type {string}
	 * @private
	 */
	private _scrollTarget: number|null|"bottom" = "bottom";

	@ViewChild('chatContainer')
	private myScrollContainer: ElementRef;

	private messageListSubscription: Subscription;

	private incomingMessageSubscription: Subscription;

	constructor(
		private chatService: ChatService
	) {
		console.log("chat - construct");
	}

	private _sortMessages() {
		this.messageList = this.messageList.sort(
			(item1: Message, item2: Message) =>
				item1.dateSent > item2.dateSent ? 1 : (
					(item1.dateSent < item2.dateSent ) ? -1 : 0));
	}

	/**
	 * After initialization it will request message list and then it will scroll down
	 * to the last message.
	 */
	ngOnInit() {
		console.log("chat - init()");
		//this.chatService.init(this.conversation.id);
		this.messageListSubscription = this.chatService.messageList$.subscribe((messageList) => {
			this.messageList = messageList;
			this._scrollTarget = "bottom";
		});
		//this.subscribeConversationMessages();
	}

	subscribeConversationMessages() {
		this.incomingMessageSubscription = this.chatService.getIncomingMessage$(this.conversation.id).subscribe((message) => {
			this.messageList.push(message);
			//this._sortMessages(); //@todo maybe not necessary
			if(!message.isIncoming) this._scrollTarget = "bottom";
		});
	}

	/**
	 * After the component is destroyed, subscription to other conversations
	 * is necessary to remove.
	 */
	ngOnDestroy() {
		this.messageListSubscription.unsubscribe();
		this.incomingMessageSubscription.unsubscribe();
	}

	/**
	 * when the user switch the conversation, it is necessary to load messages of selected conversation
	 * It is also necessary to unsubscribe from listening messages from old conversation and subscribe
	 * to listening of new conversation messages.
	 * @param changes
	 */
	ngOnChanges(changes: {[propertyName: string]: SimpleChange}) {
		if(changes.hasOwnProperty('conversation') && this.conversation
			&& changes["conversation"].previousValue !== changes["conversation"].currentValue) {
			//@todo compare ids, not whole conversations
			if(this.incomingMessageSubscription) this.incomingMessageSubscription.unsubscribe();
			this.subscribeConversationMessages();
			this.chatService.updateConversationMessages(this.conversation.id);
		}
	}

	/**
	 * After the view is redrawn, it will check the value of scrollTarget. If it is not
	 * null, it will scroll to the bottom or to the defined offset.
	 */
	ngAfterViewChecked() {
		if(this._scrollTarget === null) return;
		if(this._scrollTarget === "bottom") this.scrollToBottom();
		else this.scrollTo(this._scrollTarget);
	}

	/**
	 * Send message and scroll the chat window to the bottom, so that the message is visible
	 */
	sendMessage() {
		this.chatService.sendMessage(this.newMessage, this.conversation.id);
		this.messageList.push(new Message(
			0,
			"",
			this.newMessage,
			new Date(),
			MessageState.Sending,
			MessageType.Outgoing));
		this.newMessage = "";
		this._scrollTarget = "bottom";
	}

	private getTypeClass(messageType: MessageType) {
		return MessageType.getIdentifier(messageType);
	}

	private getStateClass(messageState: MessageState) {
		return "";
		//return MessageState.getIdentifier(messageState);
	}

	/**
	 * set scrollTarget - the chat will be scrolled when the component will be redrawn
	 * @param target
	 */
	setScrollTarget(target) {
		this._scrollTarget = target;
	}

	/**
	 * Perform scroll to the bottom and reset scrollTarget
	 */
	private scrollToBottom(): void {
		this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
		this._scrollTarget = null;
	}

	/**
	 * Perform croll to defined offset and reset "sign for future scroll" defined by scrollTarget
	 * @param scrollOffset
	 */
	private scrollTo(scrollOffset) {
		this.myScrollContainer.nativeElement.scrollTop = scrollOffset;
		this._scrollTarget = null;
	}

	/**
	 * Returns author of message - if it is outgoing, it writes empty author
	 * @param message
	 * @returns {string}
	 */
	getMessageAuthor(message: Message): string {
		return message.isIncoming ? message.senderName : "";
	}
}
