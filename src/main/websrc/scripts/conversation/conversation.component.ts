import {Component, OnInit, Input} from '@angular/core';
//import {ConversationService} from "./conversation.service";
import {ConversationEntity} from "./conversation-entity";
import {InfoView} from "./info-view.interface";

@Component({
  selector: 'conversation',
  template: `<div class="conversation">
				<info [data]="conversationInfo" [view]="view">
					<ng-content></ng-content>
				</info>
				<chat *ngIf="conversationInfo" [conversation]="conversationInfo.conversation"></chat>
			</div>`
})
export class ConversationComponent implements OnInit {

	@Input("info")
	conversationInfo: ConversationEntity;

	@Input("view")
	view: InfoView;

	private documentList: any[];

	constructor(
		//private conversationService: ConversationService
	) {
		console.log("conversation - construct");
	}

	ngOnInit() {
		console.log("conversation - init() "+this.conversationInfo.conversation.id);
		//this.conversationService.init();
		/*this.conversationService.conversation$.subscribe((conversation) => {
			this.conversation = conversation;
		});*/
	}

	ngOnChanges(changes) {
		console.log("conversation - changes of input", changes);
		/*if(changes.hasOwnProperty('conversationId') && this.conversationId) {
			this.conversationService.updateConversation(this.conversationId);
		}*/
	}

}
