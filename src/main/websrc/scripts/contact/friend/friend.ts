import {ConversationEntity} from "../../conversation/conversation-entity";
import {Conversation} from "../../conversation/conversation";
import {ConversationMember} from "../../conversation/conversation-member";

export class Friend extends ConversationEntity {

	static fromJson(jsonObject: any): Friend {
		let friend = new Friend();
		friend.id = jsonObject.id;
		friend.name = jsonObject.name;
		friend.conversation = jsonObject.conversation ? Conversation.fromJson(jsonObject.conversation) : null;
		return friend;
	}

	get friend(): ConversationMember {
		return this.conversation.memberList[0];
	}

	isVoiceCallPermitted(): boolean {
		return true;
	}

	isVideoCallPermitted(): boolean {
		return true;
	}

	isWebinarPermitted(): boolean {
		return false;
	}
}
