import {ConversationEntity} from "../../conversation/conversation-entity";
import {Conversation} from "../../conversation/conversation";

export class GroupTopic extends ConversationEntity {

	static fromJson(jsonObject: any): GroupTopic {
		let groupTopic = new GroupTopic();
		groupTopic.id = jsonObject.id;
		groupTopic.name = jsonObject.name;
		groupTopic.conversation = Conversation.fromJson(jsonObject.conversation);
		return groupTopic;
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
