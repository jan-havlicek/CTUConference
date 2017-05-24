import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {Group} from "./group";
import {ContactListService} from "../contact-list/contact-list.service";
import {MembershipRole} from "./membership-role.enum";
import {GroupMembership} from "./group-membership";
import {GroupService} from "./group.service";
import {FlashMessageService} from "../../app/flash-message.service";
import {MembershipState} from "./membership-state.enum";
import {InfoView} from "../../conversation/info-view.interface";

@Component({
	selector: 'group',
	template: `<conversation *ngIf="group" [info]="group" [view]="infoView">
					<nav>
						<button *ngIf="!group.isOnlyAdmin" pButton (click)="leaveGroup(group)">Leave group</button>
						<button *ngIf="canAdministrate(group)" pButton (click)="removeGroup(group)">Remove group</button>
					</nav>

					<!--div class="overview">
						<h3>Seznam událostí</h3>
						<h3>Seznam témat</h3>
					</div-->
					<div *ngIf="canAdministrate(group)" class="group-admin">
						<p-dataTable *ngIf="memberList?.length > 0" [value]="memberList" [rows]="10" [paginator]="true" [pageLinks]="3"
						[rowsPerPageOptions]="[5,10,20]">
							<p-column field="userName" header="Name"></p-column>
							<p-column field="email" header="E-mail"></p-column>
							<p-column field="state" header="State">
								<template let-col let-member="rowData" pTemplate="body">
									<span>{{getMembershipState(member[col.field])}}</span>
									<!--div *ngIf="isRequested(member[col.field])" class="buttons">
										<p-dropdown [options]="roleOptions" [(ngModel)]="member.role"></p-dropdown>
										<button type="button" pButton (click)="acceptMembership(member)" icon="fa-check" title="Accept membership"></button>
										<button type="button" pButton (click)="rejectMembership(member)" icon="fa-close" title="Reject membership"></button>
									</div>
									<div *ngIf="isAccepted(member[col.field])" class="buttons">
										<button type="button" pButton (click)="removeMembership(member)" icon="fa-close" title="Reject membership"></button>
									</div>
									<div *ngIf="isRejected(member[col.field])" class="buttons">
										<p-dropdown [options]="roleOptions" [(ngModel)]="member.role"></p-dropdown>
										<button type="button" pButton (click)="acceptMembership(member)" icon="fa-check" title="Accept membership"></button>
									</div-->
								</template>
							</p-column>
							<p-column field="role" header="Role">
								<template let-col let-member="rowData" pTemplate="body">
									<span class="role">{{getMemberRole(member[col.field])}}</span>
									<!--div *ngIf="isAccepted(member.state)">
										<p-dropdown [options]="roleOptions" [(ngModel)]="member.role"></p-dropdown>
										<button type="button" pButton (click)="changeRole(member)" icon="fa-edit" title="Change user role"></button>
									</div-->
								</template>
							</p-column>
						</p-dataTable>
						<p *ngIf="memberList.length === 0" class="empty-memberlist">There are no members in this group yet</p>
					</div>
				</conversation>`
})
export class GroupComponent implements OnInit, OnDestroy {

	group: Group;

	memberList: GroupMembership[] = [];

	groupSubscription;

	roleOptions: {label: string, value: any}[];

	/**
	 * Type of display info about contact. It can be displayed chat or detail
	 */
	infoView: InfoView;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private contactListService: ContactListService,
		private groupService: GroupService,
		private flashMessageService: FlashMessageService
	) {
	}

	ngOnInit() {
		this.route.params.forEach((params: Params) => {
			let groupId = +params["id"];
			let view = !params["view"] ? "" : params["view"];
			if(view !== "chat" && view !== "detail" && view !== "") this.router.navigate(['/dashboard']);
			this.infoView = {baseUrl: `/group/${groupId}`, view: view};
			this.groupSubscription = this.contactListService.getGroup$(groupId).subscribe((group) => {
				// if(!group) this.router.navigate(["/dashboard"]);
				this.group = group;
				this.roleOptions = this.group ? MembershipRole.getOptionList(this.group.groupType) : [];
				if(group && this.canAdministrate(group)) {
					this.loadMemberList(group.id);
				}
			});
		});
	}

	ngOnDestroy() {
		this.groupSubscription && this.groupSubscription.unsubscribe();
	}

	loadMemberList(groupId: number) {
		this.groupService.loadMemberList(groupId).then(memberList => this.memberList = memberList);
	}

	/**
	 * Leave the group and redirect to dashboard
	 * The server will respond with the notification about removed group. After then the group will
	 * be removed from contact list.
	 * @param group
	 */
	leaveGroup(group: Group) {
		this.groupService.leaveGroup(group.id);
		this.router.navigate(["/dashboard"]);
		this.flashMessageService.pushMessage({severity: "info", summary: group.name, detail: "You left this group"})
	}

	/**
	 * If the user is administrator and is not the only administrator, then the user can remove
	 * whole group.
	 * @todo co kdyz odeberu skupinu, ktera zrovna vysila, nepodrezu si vetvicku, kdyz budu chtit prejit na konverzaci?
	 * @param group
	 */
	removeGroup(group: Group) {
		this.groupService.removeGroup(group);
	}

	acceptMembership(member: GroupMembership) {
		this.groupService.acceptMembership(member.userId, this.group.id, member.role);
		member.state = MembershipState.Accepted;
		this.flashMessageService.pushMessage({severity: "info", summary: member.userName, detail: "Member was accepted"})
	}

	/**
	 * Reject the membership after the user requested the membership.
	 * @param member
	 */
	rejectMembership(member: GroupMembership) {
		this.groupService.rejectMembership(member.userId, this.group.id);
		member.state = MembershipState.Rejected;
		this.flashMessageService.pushMessage({severity: "info", summary: member.userName, detail: "Member was rejected"})
	}

	/**
	 * Remove the membership of selected user. The user will be no longer member of this group
	 * @param member
	 */
	removeMembership(member: GroupMembership) {
		this.groupService.removeMembership(member.userId, this.group.id);
		member.state = MembershipState.Removed;
		this.flashMessageService.pushMessage({severity: "info", summary: member.userName, detail: "Member was removed"})
	}

	/**
	 * If the user is accepted, then it will change the role of the user in the group
	 * @param member
	 */
	changeRole(member: GroupMembership) {
		this.groupService.changeRole(member.userId, this.group.id, member.role);
	}

	/**
	 * Can user administrate the group?
	 * @param group
	 * @returns {boolean}
	 */
	canAdministrate(group: Group): boolean {
		return group.myRole == MembershipRole.Admin;
	}

	/**
	 * If the user is not accepted, it hasn't any role assigned yet
	 * @param role
	 * @returns {any}
	 */
	getMemberRole(role: MembershipRole): string {
		if(!role) return "";
		return MembershipRole.getLabel(role);
	}

	getMembershipState(state: MembershipState): string {
		return MembershipState.getLabel(state);
	}

	isRequested(state: MembershipState): boolean {
		return state === MembershipState.Requested;
	}

	isAccepted(state: MembershipState): boolean {
		return state === MembershipState.Accepted;
	}

	isRejected(state: MembershipState): boolean {
		return state === MembershipState.Rejected || state === MembershipState.Removed;
	}
}
