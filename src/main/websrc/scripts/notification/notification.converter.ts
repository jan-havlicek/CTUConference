import {Injectable} from "@angular/core";
import {Notification} from "./notification";
import {RelationshipAction} from "./relationship-action.enum";
import {FriendService} from "../contact/friend/friend.service";
import {GroupService} from "../contact/group/group.service";
import {MembershipRole} from "../contact/group/membership-role.enum";
import {Router} from "@angular/router";

@Injectable()
export class NotificationConverter {

	constructor(
		private friendService: FriendService,
		private groupService: GroupService,
		private router: Router
	) {

	}

	public fromJson(jsonData): Notification {
		if(jsonData.friendId) {
			return this.loadFriendNotification(jsonData);
		} else if(jsonData.groupId && jsonData.userId) {
			return this.loadGroupNotification(jsonData);
		}

		return null;
	}

	private loadFriendNotification(jsonData): Notification {
		let notification = new Notification();
		notification.id = jsonData.id;
		notification.date = jsonData.dateCreated;
		notification.title = jsonData.friendName;
		notification.isRead = jsonData.isRead;
		switch(jsonData.action) {
			case RelationshipAction.Requested:
				notification.message = "wants to connect with you.";
				notification.actions = [
					{title: "Accept", action: () => this.friendService.acceptContact(jsonData.friendId)},
					//{title: "Reject", action: () => this.friendService.rejectContact(jsonData.friendId)},
				];
				break;
			case RelationshipAction.Accepted:
				notification.message = "accepted your friend request.";
				notification.actions = [
					{title: "Show", action: () => this.router.navigate(['/friend/'+jsonData.friendId])}
				];
				break;
			case RelationshipAction.Rejected:
				notification.message = "rejected your friend request.";
		}
		return notification;
	}

	private loadGroupNotification(jsonData): Notification {
		let notification = new Notification();
		notification.id = jsonData.id;
		notification.date = jsonData.dateCreated;
		notification.isRead = jsonData.isRead;
		notification.title = jsonData.userName;
		switch(jsonData.action) {
			case RelationshipAction.Requested:
				notification.message = `wants to connect to group "${jsonData.groupName}".`;
				notification.actions = [
					{
						title: "Accept",
						action: (option) => this.groupService.acceptMembership(jsonData.userId,  jsonData.groupId, option),
						options: MembershipRole.getOptionList(jsonData.groupType)
					},
					// {
					// 	title: "Reject",
					// 	action: () => this.groupService.rejectMembership(jsonData.userId, jsonData.groupId)
					// },
				];
				break;
			case RelationshipAction.Accepted:
				notification.message = "accepted your group access request.";
				notification.actions = [
					{title: "Show group", action: () => this.router.navigate(["/group/"+jsonData.groupId])}
				];
				break;
			case RelationshipAction.Rejected:
				notification.message = "rejected your group access request.";
		}
		return notification;
	}
}
