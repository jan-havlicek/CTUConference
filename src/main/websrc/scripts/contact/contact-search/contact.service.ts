import {Injectable} from "@angular/core";
import {SocketMessageService} from "../../communication/socket-message.service";
import {SocketMessage} from "../../communication/socket-message";
import {Friend} from "../friend/friend";
import {Group} from "../group/group";

@Injectable()
export class ContactService {

	constructor(
		private messageService: SocketMessageService
	) {
	}

	filterContacts(filter: string) {
		return new Promise<[Friend|Group]>((resolve, reject) => {
			let message = new SocketMessage("contact.filter", {"filter": filter});
			this.messageService.request(message, "filter").then((response: SocketMessage) => {
				let contacts = response.data.results.map((contact) =>
					contact.type === "friend"
						? Friend.fromJson(contact) : Group.fromJson(contact));
				resolve(contacts);
			});
		});
	}
}
