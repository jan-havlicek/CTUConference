import {SocketMessage} from "../../communication/socket-message";
import {Injectable} from "@angular/core";
import {SocketMessageService} from "../../communication/socket-message.service";
import {Group} from "./group";
import {Subject, Observable} from "rxjs";
import {GroupMembership} from "./group-membership";
import {MembershipRole} from "./membership-role.enum";
import {Router} from "@angular/router";
import {FlashMessageService} from "../../app/flash-message.service";

@Injectable()
export class GroupService {
	protected _suggestions$: Subject<Group[]>;

	constructor(
		private messageService: SocketMessageService,
		private router: Router,
		private flashMessageService: FlashMessageService
	) {
		this._suggestions$ = <Subject<Group[]>>new Subject();
	}

	loadGroup(groupId) {
		this.messageService.send(new SocketMessage("group.get", {groupId: groupId}));
	}

	requestMembership(groupId: number): void {
		this.messageService.send(new SocketMessage("group.request", {groupId: groupId}));
	}

	acceptMembership(userId: number, groupId: number, role: MembershipRole): void {
		this.messageService.send(new SocketMessage("group.accept", {userId: userId, groupId: groupId, type: "group", role: role}));
	}

	rejectMembership(userId: number, groupId: number): void {
		this.messageService.send(new SocketMessage("group.reject", {userId: userId, groupId: groupId}));
	}

	changeRole(userId: number, groupId: number, role: MembershipRole): void {
		this.messageService.send(new SocketMessage("group.change-role", {userId: userId, groupId: groupId, type: "group", role: role}));
	}

	removeMembership(userId: number, groupId: number): void {
		this.messageService.send(new SocketMessage("group.remove-membership", {userId: userId, groupId: groupId}));
	}

	leaveGroup(groupId: number): void {
		this.messageService.send(new SocketMessage("group.leave", {groupId: groupId}));
	}

	/** Contact suggestion functionality **/

	loadSuggestions() {
		let message = new SocketMessage("group.suggest", {});
		this.messageService.request(message).then((result) => {
			let suggestions = result.data.suggestions.map(item => Group.fromJson(item));
			this._suggestions$.next(suggestions);
		});
	}

	get suggestions$(): Observable<Group[]> {
		return this._suggestions$.asObservable();
	}

	loadMemberList(groupId: number): Promise<GroupMembership[]> {
		return new Promise<GroupMembership[]>((resolve, reject) => {
			let message = new SocketMessage("group.memberships", {groupId: groupId});
			this.messageService.request(message).then((result) => {
				let memberList = result.data.map(item => GroupMembership.fromJson(item));
				resolve(memberList);
			});
		});
	}

	createGroup(group: Group) {
		let message = new SocketMessage("group.create", {name: group.name, type: group.groupType});
		this.messageService.request(message).then(result => {
				this.router.navigate([`/group/${result.data.id}`]);
				this.flashMessageService.pushMessage({severity: "success", summary: group.name, detail: "Group was created"});
			}
		);
	}

	removeGroup(group: Group) {
		let message = new SocketMessage("group.remove", {groupId: group.id});
		this.messageService.request(message).then(result => {
				// this.router.navigate([`/dashboard`]);
				// this.flashMessageService.pushMessage({severity: "success", summary: group.name, detail: "Group was removed"});
			}
		);
	}
}
