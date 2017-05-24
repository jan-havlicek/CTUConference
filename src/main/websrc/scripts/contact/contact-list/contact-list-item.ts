import {Friend} from "../friend/friend";
import {GroupTopic} from "../group/group-topic";
import {GroupEvent} from "../group/group-event";
import {ConversationEntity} from "../../conversation/conversation-entity";
import {CallType} from "../../conversation/call/call-type.enum";
import {Transmission} from "../../conversation/call/trasmission";

export class ContactListItem {

	private _friend: Friend;
	private _groupTopic: GroupTopic;
	private _groupEvent: GroupEvent;

	static fromJson(jsonObject: any): ContactListItem {
		let listItem = new ContactListItem();
		listItem.friend = null;
		listItem.groupTopic = null;
		listItem.groupEvent = null;
		if(jsonObject.friend) {
			listItem.friend = Friend.fromJson(jsonObject.friend);
		} else if(jsonObject.groupTopic) {
			listItem.groupTopic = GroupTopic.fromJson(jsonObject.groupTopic);
		} else if(jsonObject.groupEvent) {
			listItem.groupEvent = GroupEvent.fromJson(jsonObject.groupEvent);
		}
		return listItem;
	}

	get type() {
		if(this.friend) {
			return "private";
		} else if(this.groupTopic) {
			return "group-topic";
		} else if(this.groupEvent) {
			return "group-event";
		} else {
			return "multichat";
		}
	}

	get url(): string {
		switch(this.type) {
			case "private": return "/friend/" + this._friend.id;
			//case "multichat": return "/multichat/" + this.conversation.id;
			case "group-topic": return "/group-topic/" + this._groupTopic.id;
			case "group-event": return "/group-event/" + this._groupEvent.id;
		}
	}

	get hasAvailableMembers() {
		return this.contact.hasAvailableMembers;
	}

	get isTransmitting() {
		return this.contact.isTransmitting;
	}

	get transmission(): Transmission {
		return this.contact.conversation.transmission;
	}

	set transmission(value: Transmission) {
		this.contact.conversation.transmission = value;
	}

	get title() {
		return this.contact.name;
	}

	get name() {
		return this.contact.name;
	}

	get contact(): ConversationEntity {
		switch(this.type) {
			case "private": return this._friend;
			//case "multichat": return "/multichat/" + this.conversation.id;
			case "group-topic": return this._groupTopic;
			case "group-event": return this._groupEvent;
		}
		return null;
	}

	get friend(): Friend {
		return this._friend;
	}

	set friend(value: Friend) {
		this._friend = value;
	}

	get groupTopic(): GroupTopic {
		return this._groupTopic;
	}

	set groupTopic(value: GroupTopic) {
		this._groupTopic = value;
	}

	get groupEvent(): GroupEvent {
		return this._groupEvent;
	}

	set groupEvent(value: GroupEvent) {
		this._groupEvent = value;
	}
}
