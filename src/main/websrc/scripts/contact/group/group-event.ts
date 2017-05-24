import {ConversationEntity} from "../../conversation/conversation-entity";
import {Conversation} from "../../conversation/conversation";

export class GroupEvent extends ConversationEntity {

	static fromJson(jsonObject: any): GroupEvent {
		let groupEvent = new GroupEvent();
		groupEvent.id = jsonObject.id;
		groupEvent.name = jsonObject.name;
		groupEvent.conversation = Conversation.fromJson(jsonObject.conversation);
		return groupEvent;
	}

	isVoiceCallPermitted(): boolean {
		return true;
	}

	isVideoCallPermitted(): boolean {
		return true;
	}

	isWebinarPermitted(): boolean {
		return true;
	}
}
