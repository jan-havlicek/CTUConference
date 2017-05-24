import {Injectable} from "@angular/core";
import {SocketMessageService} from "../../communication/socket-message.service";
import {GroupEvent} from "./group-event";
import {Subject} from "rxjs";
import {AbstractConversationEntityService} from "../contact-list/abstract-conversation-entity.service";

@Injectable()
export class GroupEventService extends AbstractConversationEntityService {

	jsonConverter = GroupEvent.fromJson;
	contactTypeName = "group-event";

	constructor(
		messageService: SocketMessageService
	) {
		super(messageService);
		this._suggestions$ = <Subject<GroupEvent[]>>new Subject();
	}
}
