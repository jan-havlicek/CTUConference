
import {NotificationAction} from "./notification-action";

/**
 * Notification is displayed on the header bar. It represents notification about friendships
 * and memberships and many other
 * The "isRead" property means, the action was performed, if the notification contains the
 * action, that should be done or that the user explicitly marked this notification as read.
 *
 * In the current version of application, read notification will no longer display to the
 * user and mark can't be undo.
 */
export class Notification {
	private _id: number;
	private _date: Date;
	private _isRead: boolean;
	private _title: string;
	private _message: string;
	private _actions: NotificationAction[];

	constructor(id?: number, date?: Date, isRead?: boolean, title?: string, message?: string) {
		this._id = id;
		this._date = date;
		this._isRead = isRead;
		this._title = title;
		this._message = message;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get date(): Date {
		return this._date;
	}

	set date(value: Date) {
		this._date = value;
	}

	get isRead(): boolean {
		return this._isRead;
	}

	set isRead(value: boolean) {
		this._isRead = value;
	}

	get title(): string {
		return this._title;
	}

	set title(value: string) {
		this._title = value;
	}

	get message(): string {
		return this._message;
	}

	set message(value: string) {
		this._message = value;
	}

	get actions(): NotificationAction[] {
		return this._actions;
	}

	set actions(value: NotificationAction[]) {
		this._actions = value;
	}
}
