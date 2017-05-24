import {ContactListContainer} from "./contact-list-container";
import {ContactState} from "./contact-state.enum";
import {ContactListItem} from "./contact-list-item";
import {CallType} from "../../conversation/call/call-type.enum";
import {ConversationMember} from "../../conversation/conversation-member";
import {Friend} from "../friend/friend";
import {ConversationEntity} from "../../conversation/conversation-entity";
import {Group} from "../group/group";
import {Transmission} from "../../conversation/call/trasmission";

export class ContactList {
	private _containers: ContactListContainer[];

	/**
	 * Create contact list from JSON object
	 * @param jsonObject
	 * @returns {ContactList}
	 */
	static fromJson(jsonObject: any): ContactList {
		let contactList = new ContactList();
		contactList.containers = jsonObject.containers
			? jsonObject.containers.map(item => ContactListContainer.fromJson(item))
			: [];
		contactList.containers.forEach((value, i, array) => {
			if(i > 0 && value.type === 'group' && array[i - 1].type !== 'group') {
				value.isFirst = true;
			}
		});
		return contactList;
	}

	get containers(): ContactListContainer[] {
		return this._containers;
	}

	set containers(value: ContactListContainer[]) {
		this._containers = value;
	}

	/**
	 * Returns info, whether the contact list is empty or not.
	 * @todo implement group events and topics detection
	 * @returns {boolean}
	 */
	isEmpty(): boolean {
		return !this.containsFriends() && !this.containsGroups();
	}

	/**
	 * Returns info, whether the contact list contains groups (group containers)
	 * @returns {boolean}
	 */
	containsGroups(): boolean {
		return this._containers.filter(container => container.isGroup()).length > 0;
	}

	/**
	 * Returns info, whether the contact list contains friends (in private container)
	 * @returns {boolean}
	 */
	containsFriends(): boolean {
		let privateContainer = this._containers.find(container =>
			container.isPrivate() && container.items.length > 0
		);
		return !!privateContainer;
	}

	/**
	 * Get private container
	 * @returns {ContactListContainer}
	 */
	private getPrivateContainer(): ContactListContainer {
		return this._containers.find(item => item.type === "private");
	}

	getFriend(friendId): Friend {
		let friendItem = this.getFriendItem(friendId);
		return friendItem ? friendItem.friend : null;
	}

	/**
	 * Get friend item from contact list.
	 * All data of this friend item can be also used in Friend component.
	 *
	 * @param friendId
	 */
	private getFriendItem(friendId): ContactListItem {
		return this.getPrivateContainer().items
			.find(item => item.friend.id === friendId);
	}

	/**
	 * Change state to online or offline
	 * @param friendId
	 * @param newState
	 * @returns {ContactListItem} friend item
	 */
	changeFriendState(friendId: number, newState: ContactState) {
		let friendItem = this.getFriendItem(friendId);
		friendItem.friend.friend.state = newState;
		return friendItem;
	}

	changeFriendTransmission(friendId: number, transmission: Transmission) {
		this.getFriendItem(friendId).transmission = transmission;
	}

	/**
	 * Add new friend to the contact list. It will be added at the end of the list
	 * @param friendItem
	 */
	addFriend(friendItem: ContactListItem) {
		this.getPrivateContainer().items.push(friendItem);
	}

	/**
	 * Remove friend from contact list by id. Returns the removed friend item.
	 * @param friendId
	 * @return ContactListItem removed friend item
	 */
	removeFriend(friendId: number) {
		let container = this.getPrivateContainer();
		let friendIndex = container.items.findIndex(item => item.friend.id === friendId);
		let friendItem = container.items[friendIndex];
		container.items.splice(friendIndex, 1);
		return friendItem;
	}

	/**
	 * Get group container for given groupId
	 * All data of this friend item can be also used in Group component.
	 * @param groupId
	 * @returns {ContactListContainer}
	 */
	getGroupContainer(groupId: number) {
		return this._containers.find(item => item.type === "group" && item.group.id == groupId);
	}

	addGroupMember(groupId: number, memberItem: ConversationMember) {
		let group = this.getGroupContainer(groupId);
		group.groupMembers.push(memberItem);
	}

	removeGroupMember(groupId: number, memberId: number) {
		let group = this.getGroupContainer(groupId);
		let memberIndex = group.groupMembers.findIndex(item => item.id === memberId);
		group.groupMembers.splice(memberIndex, 1);
	}

	changeGroupMemberState(groupId: number, memberId: number, newState: ContactState) {
		let group = this.getGroupContainer(groupId);
		group.groupMembers.find(item => item.id === memberId).state = newState;
	}

	addGroup(groupItem: ContactListContainer) {
		this._containers.push(groupItem);
	}

	/**
	 * Removes group by id and returns the removed container
	 * @param groupId
	 * @returns {ContactListContainer}
	 */
	removeGroup(groupId: number) {
		let containerIndex = this._containers.findIndex(item => item.type === "group" && item.group.id === groupId);
		let groupContainer = this._containers[containerIndex];
		this._containers.splice(containerIndex, 1);
		return groupContainer;
	}

	/**
	 * Find member by their name
	 * @param memberId
	 * @returns {string}
	 */
	findMemberNameById(memberId: number): string {
		let member: ConversationMember = this._containers.map(container => {
			if(container.type === "private") {
				return container.items.map(item => item.friend.conversation.memberList[0]);
			} else if(container.type === "group") {
				return container.groupMembers;
			} else {
				// @todo implement group events
				return [];
			}
		}).reduce((item1, item2) => item1.concat(item2))
			.find(member => member.id === memberId);
		return member ? member.fullName : "";
	}

	/**
	 * Find contact by conversation id (it comes from incoming call or etc.)
	 * @param conversationId
	 * @returns {ConversationEntity}
	 */
	findContact(conversationId: number): ConversationEntity {
		return this.contacts.find((contact: ConversationEntity) => contact.conversation.id === conversationId);
	}

	/**
	 * Get all contacts in contact list - groups and friends
	 * @returns {ConversationEntity[]}
	 */
	get contacts(): ConversationEntity[] {
		return this._containers
			.map(container => !container.isGroup()
				? container.items.map(containerItem => containerItem.contact) : [container.group])
			.reduce((a, b) => a.concat(b));
	}

	/**
	 * Get all contact, that has active call
	 * @returns {ConversationEntity[]}
	 *
	get transmittingGroups() {
		return this.contacts.filter(contact => contact.isTransmitting && contact instanceof Group);
	}*/

	/**
	 * Change transmission state with its type
	 * @param groupId
	 * @param transmission
	 */
	changeGroupTransmission(groupId: number, transmission: Transmission) {
		this.getGroupContainer(groupId).group.conversation.transmission = transmission;
	}

}
