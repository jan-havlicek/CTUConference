import {Injectable} from "@angular/core";
import {Subject, Observable, BehaviorSubject} from "rxjs";
import {ContactList} from "./contact-list";
import {SocketMessageService} from "../../communication/socket-message.service";
import {ContactListItem} from "./contact-list-item";
import {ContactState} from "./contact-state.enum";
import {ContactAction} from "./contact-action.enum";
import {ConversationMember} from "../../conversation/conversation-member";
import {Friend} from "../friend/friend";
import {Group} from "../group/group";
import {ContactListContainer} from "./contact-list-container";
import {FlashMessageService} from "../../app/flash-message.service";
import {Router} from "@angular/router";
import {ConversationEntity} from "../../conversation/conversation-entity";
import {CallType} from "../../conversation/call/call-type.enum";
import {CallState} from "../../conversation/call/call-state.enum";
import {Transmission} from "../../conversation/call/trasmission";
import {CallInfo} from "../../conversation/call/call-info";

@Injectable()
export class ContactListService {

	private _contactList: ContactList;

	private _contactList$: Subject<ContactList>;

	constructor(
		private messageService: SocketMessageService,
		private flashMessageService: FlashMessageService,
		private router: Router
	) {
		this._contactList$ = <BehaviorSubject<ContactList>>new BehaviorSubject(null);
		this.messageService.subscribeToDataStream("contact.*", (result) => {
			if(result.type === "contact.friend-state") {
				this.updateFriend(result.data);
			} else if(result.type === "contact.group-member-state") {
				this.updateGroupMember(result.data);
			} else if(result.type === "contact.group-state") {
				this.updateGroup(result.data);
			} else if(result.type === "contact.conversation-state") {
				this.updateConversationState(result.data);
			};
		});
	}

	init(isLogged) {
		if(isLogged) {
			this.updateContactList();
		} else {
			this._contactList = null;
			this._contactList$.next(this._contactList);
		}
	}

	/**
	 * Add, remove friend or change their state
	 * @param data
	 */
	updateFriend(data: {friendId: number, action: ContactAction, state: ContactState, friendItem?: ContactListItem}) {
		if(data.action === ContactAction.Added) {
			let friendItem = ContactListItem.fromJson(data.friendItem);
			this._contactList.addFriend(friendItem);
			this.flashMessageService.pushMessage({severity: "info", summary: friendItem.friend.friend.fullName, detail: "Friendship is accepted"});
		} else if(data.action === ContactAction.Removed) {
			// @todo route only when on the friend site.
			this.router.navigate([`/dashboard`]).then(result => {
				let friendItem = this._contactList.removeFriend(data.friendId);
				this._contactList$.next(this._contactList);
				this.flashMessageService.pushMessage({severity: "info", summary: friendItem.friend.friend.fullName, detail: "User removed friendship"});
			});
		} else {
			let friendItem = this._contactList.changeFriendState(data.friendId, data.state);
			this.flashMessageService.pushMessage({severity: "info", summary: friendItem.friend.friend.fullName, detail: "User is now online"});
		}

		this._contactList$.next(this._contactList);
	}

	/**
	 * Add, remove group member or change its state
	 * @param data
	 */
	updateGroupMember(data: {groupId: number, action: ContactAction,
			state: ContactState, memberId?: number, member?: any, conversationId?: number}) {
		if(data.action === ContactAction.Added) {
			let memberItem = ConversationMember.fromJson(data.member);
			let groupId = this.findContact(data.conversationId).id;
			this._contactList.addGroupMember(groupId, memberItem);
		} else if(data.action === ContactAction.Removed) {
			let groupId = this.findContact(data.conversationId).id;
			this._contactList.removeGroupMember(groupId, data.memberId);
		} else {
			this._contactList.changeGroupMemberState(data.groupId, data.memberId, data.state);
		}

		this._contactList$.next(this._contactList);
	}

	/**
	 * Currently only remove group from contact list.
	 * @param data
	 */
	updateGroup(data: {groupId?: number, action: ContactAction, groupContainer}) {
		if(data.action === ContactAction.Removed) {
			// @todo route only when on the group site.
			this.router.navigate([`/dashboard`]).then(result => {
				let groupContainer = this._contactList.removeGroup(data.groupId);
				this._contactList$.next(this._contactList);
				this.flashMessageService.pushMessage({severity: "success", summary: groupContainer.group.name, detail: "Group was removed"});
			});
		} else if(data.action === ContactAction.Added) {
			this._contactList.addGroup(ContactListContainer.fromJson(data.groupContainer));
			this._contactList$.next(this._contactList);
		}
	}

	updateConversationState(data: {callState: string, callType: string, conversationId: number, callInfo}) {
		let contact = this._contactList.findContact(data.conversationId);
		let transmission = new Transmission(
			data.callType ? CallState.get(data.callState) : null,
			data.callType ? CallType.get(data.callType) : null,
			data.callInfo ? CallInfo.fromJson(data.callInfo) : null
		);
		if(contact instanceof Group) {
			this._contactList.changeGroupTransmission(contact.id, transmission);
		} else {
			this._contactList.changeFriendTransmission(contact.id, transmission);
		}
	}

	/**
	 *
	 * @returns {Observable<ContactList>}
	 */
	get contactList$(): Observable<ContactList> {
		return this._contactList$.asObservable();
	}

	/**
	 * Returns Friend object for selected friend. It contains all information
	 * about the friend contact including current activity information
	 * @param friendId
	 * @returns {Observable<ContactListItem>}
	 */
	getFriend$(friendId: number): Observable<Friend> {
		return this._contactList$.map(contactList => {
			if(contactList) {
				let friend = contactList.getFriend(friendId);
				if(friend) {
					return friend;
			 	} else {
					this.router.navigate(["/dashboard"]);
					this.flashMessageService.pushMessage({severity: "warn", summary: "Friend not found", detail: "Friend is not in your contact list"})
				}
			}
			return null;
		});
	}

	getGroup$(groupId: number): Observable<Group> {
		return this._contactList$.map(contactList => {
			if(contactList) {
				let groupContainer = contactList.getGroupContainer(groupId);
				if(groupContainer) {
					return groupContainer.group;
				} else {
					this.router.navigate(["/dashboard"]);
					this.flashMessageService.pushMessage({severity: "warn", summary: "Group not found", detail: "Group is not in your contact list"})
				}
			}
			return null;
		})
	}

	/**
	 * Returns observable name of the member by their id.
	 * @param participantId
	 * @returns {Observable<string>}
	 */
	getMemberName$(participantId: number): Observable<string> {
		return this._contactList$.map(contactList => {
			return contactList ? contactList.findMemberNameById(participantId) : "";
		})
	}

	/**
	 * Returns observable ongoing calls - contacts with calls that are currently available to join
	 * @returns {Observable<ConversationEntity[]>}
	 */
	get ongoingCalls$() {
		return this._contactList$.map(contactList => {
			if(contactList === null) return [];
			return contactList.containers.filter(container => container.isGroup() && container.isTransmitting)
				.map(container => container.group);
		});
	}

	/**
	 * Returns observable information about whether the contact list is empty or not
	 * Empty means that it has no groups or friends
	 * @returns {Observable<boolean>}
	 */
	get isEmpty$(): Observable<boolean> {
		return this._contactList$.map(contactList => !contactList || contactList.isEmpty());
	}

	private updateContactList() {
		this.messageService.request("contact.list").then((result) => {
			let contactList = ContactList.fromJson(result.data);
			//this.synchronizeToLocalStorage(result.data);
			this._contactList = contactList;
			this._contactList$.next(contactList);
		});
	}

	/**
	 * Find contact (friend or group) by its conversation id
	 * @param conversationId
	 * @returns {ConversationEntity}
	 */
	public findContact(conversationId): ConversationEntity {
		return this._contactList.findContact(conversationId);
	}
}
