
import {Injectable} from "@angular/core";
import {Registration} from "./registration";
import {SocketMessage} from "../communication/socket-message";
import {SocketMessageService} from "../communication/socket-message.service";

@Injectable()
export class RegistrationService {

	constructor(
		private messageService: SocketMessageService
	){}

	register(registration: Registration) {
		this.messageService.send(new SocketMessage("registration.register", {registration: registration}));
	}
}
