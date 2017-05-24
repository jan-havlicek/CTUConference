import {Notification} from "./notification";
import {Subject} from "rxjs";
import {SocketMessageService} from "../communication/socket-message.service";
import {NotificationConverter} from "./notification.converter";
import {SocketMessage} from "../communication/socket-message";
import {Injectable} from "@angular/core";
import {FlashMessageService} from "../app/flash-message.service";

@Injectable()
export class NotificationService {

	private _notifications: Notification[];

	private _notifications$: Subject<Notification[]>;

	constructor(
		private messageService: SocketMessageService,
		private notificationConverter: NotificationConverter,
		private flashMessageService: FlashMessageService
	) {
		this._notifications = [];
		this._notifications$ = <Subject<Notification[]>>new Subject();
		this.messageService.subscribeToDataStream("notification.*", result => {
			if(result.type !== "notification.list") {
				let notification = notificationConverter.fromJson(result.data);
				this.flashMessageService.pushMessage({severity: "info", summary: notification.title, detail: notification.message + " (more info in notification list)"});
				this._notifications.unshift(notification);
				this._notifications$.next(this._notifications);
			}
		});
	}

	updateNotificationList() {
		let message = new SocketMessage("notification.list");
		this.messageService.request(message).then(result => {
			this._notifications = result.data.map(item => this.notificationConverter.fromJson(item));
			this._notifications$.next(this._notifications);
		})
	}
    //
	// add(notification: Notification) {
	// 	this._notifications$.next(notification);
	// }

	get notifications$() {
		return this._notifications$.asObservable();
	}

	markRead(notification: Notification) {
		notification.isRead = true;
		this.messageService.send(new SocketMessage("notification.mark-read", {notificationId: notification.id}));
	}

	markUnread(notification: Notification) {
		notification.isRead = false;
		this.messageService.send(new SocketMessage("notification.mark-unread", {notificationId: notification.id}));
	}
}
