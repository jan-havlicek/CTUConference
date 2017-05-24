import {Component, OnInit} from "@angular/core";
import {NotificationService} from "./notification.service";
import {Notification} from "./notification";

@Component({
	selector: 'notifications',
	template: `<div class="notifications">
					<div class="notification-icon" [ngClass]="{unread: containUnread()}"
						(click)="notificationList.show($event); onNotificationShow()">
						<svg class="icon">
						  <use xlink:href="#notification-bell"></use>
						</svg>
						<span class="unread-count">{{unreadCount}}</span>
					</div>
					<p-overlayPanel #notificationList styleClass="notification-list">
						<p-dataList *ngIf="notifications?.length > 0" [value]="notifications">
							<template let-notification pTemplate="body">
								<div [ngClass]="{unread: !notification.isRead}">
									<header>{{notification.title}}</header>
									<span *ngIf="!notification.isRead" class="mark-read" (click)="markRead(notification)" title="Mark read"></span>
									<!--span *ngIf="notification.isRead" class="mark-unread" (click)="markUnread(notification)" title="Mark unread"></span-->
									<p>{{notification.message}}</p>
									<div *ngIf="notification.actions" class="buttons">
										<p *ngIf="notification.isRead">Notification is finished</p>
										<div *ngIf="!notification.isRead">
											<button *ngFor="let notificationAction of notification.actions"
												(click)="notificationAction.action(); markRead(notification)">
												{{notificationAction.title}}
											</button>
										</div>
									</div>
									<time>{{notification.date | date:'short'}}</time>
								</div>
							</template>
						</p-dataList>
						<p *ngIf="!notifications || !notifications.length">There are no notifications</p>
					</p-overlayPanel>
				</div>`
})
export class NotificationComponent implements OnInit {

	notifications: Notification[];

	constructor(
		private notificationService: NotificationService
	) {
		this.notifications = [];
		notificationService.notifications$.subscribe(notifications => this.notifications = notifications);
	}

	ngOnInit() {
		this.notificationService.updateNotificationList();
	}

	/**
	 * True if it contain at least one unread notification
	 * @returns {boolean}
	 */
	containUnread(): boolean {
		return this.unreadCount > 0;
	}

	get unreadCount(): number {
		return this.notifications.filter(item => !item.isRead).length;
	}

	onNotificationShow() {
		/* this.notifications.forEach(item => {
			if(!item.isRead) {
				this.notificationService.markRead(item.id);
				item.isRead = true;
			}
		});*/
	}

	markUnread(notification: Notification) {
		this.notificationService.markUnread(notification);
	}

	markRead(notification: Notification) {
		this.notificationService.markRead(notification);
	}
}
