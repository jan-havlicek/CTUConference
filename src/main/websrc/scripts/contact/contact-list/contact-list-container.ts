import {ContactListItem} from "./contact-list-item";
import {Group} from "../group/group";
import {MembershipRole} from "../group/membership-role.enum";
import {Transmission} from "../../conversation/call/trasmission";
import {CallState} from "../../conversation/call/call-state.enum";

/**
 * Contact list container contains type, items and if it
 * this contact list group is real Group, groupId and conversationId
 * is provided in order to create link to it and notify new messages.
 */
export class ContactListContainer {
	protected _type: string; //private|multichat|group
	protected _items: ContactListItem[];

	private _group: Group;
	private _isFirst: boolean; //it it is first group container

	/**
	 * Create contact list container from JSON object
	 * @param jsonObject
	 * @returns {ContactListContainer}
	 */
	static fromJson(jsonObject: any): ContactListContainer {
		let listContainer = new ContactListContainer();
		listContainer.type = jsonObject.type;
		if(listContainer.type === "group") {
			listContainer.group = Group.fromJson(jsonObject.group);
			listContainer.group.myRole = MembershipRole.get(jsonObject.groupRole + "");
			listContainer.group.isOnlyAdmin = jsonObject.isOnlyAdmin;
		}
		listContainer.items = jsonObject.items
			? jsonObject.items.map(item => ContactListItem.fromJson(item))
			: [];
		listContainer.isFirst = false;
		return listContainer;
	}


	/**
	 * Returns info, whether there is somebody online to make the call to
	 * @returns {boolean}
	 */
	get hasAvailableMembers() {
		if(this.type === "group") { //is group item
			return this._group.conversation.hasAvailableMembers;
		}
		return false;
	}

	get isTransmitting() {
		if(this.type === "group") { //is group item
			return this._group.conversation.isTransmitting;
		}
		return false;
	}

	get transmission(): Transmission {
		if(this.type === "group") { //is group item
			return this._group.conversation.transmission;
		}
		return new Transmission(CallState.None, null, null);
	}

	get label(): string {
		switch(this._type) {
			case "private": return "Friends";
			case "multichat": return "Multichats";
			case "group": return this.group.name;
		}
		throw new Error('unknown contact list container type: ' + this._type);
	}

	get url(): string {
		if(this._type === 'group') return "/group/" + this._group.id;
		return "";
	}

	get groupMembers() {
		if(this.type !== "group") throw new Error('Cannot find group members');
		return this._group.conversation.memberList;
	}

	get type(): string {
		return this._type;
	}

	set type(value: string) {
		this._type = value;
	}

	get items(): ContactListItem[] {
		return this._items;
	}

	set items(value: ContactListItem[]) {
		this._items = value;
	}

	get group(): Group {
		return this._group;
	}

	set group(value: Group) {
		this._group = value;
	}

	get isFirst(): boolean {
		return this._isFirst;
	}

	set isFirst(value: boolean) {
		this._isFirst = value;
	}

	isPrivate(): boolean {
		return this._type === "private";
	}

	isGroup(): boolean {
		return this._type === "group";
	}
}
