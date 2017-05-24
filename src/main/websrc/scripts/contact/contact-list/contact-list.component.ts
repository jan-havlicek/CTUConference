import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "../../authentication/authentication.service";
import {ContactListService} from "./contact-list.service";
import {ContactList} from "./contact-list";
import {CallType} from "../../conversation/call/call-type.enum";
import {Router} from "@angular/router";
import {Transmission} from "../../conversation/call/trasmission";

@Component({
  selector: 'contact-list',
  template: `<div class="contact-list">
			<p-dataList [value]="contactList.containers" *ngIf="contactList && contactList.containers.length > 0">
				<template let-contactGroup pTemplate="item">
					<h3 *ngIf="contactGroup.isFirst">Groups</h3>
					<div class="contact-item" [ngClass]="contactGroup.type">
						<h3 *ngIf="contactGroup.type != 'group'">{{contactGroup.label}}</h3>
						<a *ngIf="contactGroup.type == 'group'" routerLink="{{contactGroup.url}}" title="{{contactGroup.label}}">{{contactGroup.label}}</a>
						<div *ngIf="contactGroup.type == 'group'" class="activity-indicators">
							<span *ngIf="!contactGroup.isTransmitting && contactGroup.hasAvailableMembers" class="indicator-state online"></span>
							<span *ngIf="contactGroup.isTransmitting" class="indicator-state transmission"
										[ngClass]="getTransmissionTypeClass(contactGroup.transmission)">
								<svg *ngIf="getTransmissionTypeClass(contactGroup.transmission) == 'voice'" class="icon">
								  <use xlink:href="#media-container--microphone"></use>
								</svg>
								<svg *ngIf="getTransmissionTypeClass(contactGroup.transmission) == 'video'" class="icon">
								  <use xlink:href="#media-container--video-camera"></use>
								</svg>
								<svg *ngIf="getTransmissionTypeClass(contactGroup.transmission) == 'webinar'" class="icon">
								  <use xlink:href="#media-container--video-lecture"></use>
								</svg>
							</span>
						</div>
						<p-dataList [value]="contactGroup.items" *ngIf="contactGroup.items.length > 0">
							<template let-contact pTemplate="item">
								<a routerLink="{{contact.url}}" title="{{contact.title}}">{{contact.name}}</a>
								<div class="activity-indicators">
									<span *ngIf="!contact.isTransmitting && contact.hasAvailableMembers" class="indicator-state online"></span>
									<span *ngIf="contact.isTransmitting" class="indicator-state transmission"
										[ngClass]="getTransmissionTypeClass(contact.transmission)">
										<svg *ngIf="getTransmissionTypeClass(contact.transmission) == 'voice'" class="icon">
										  <use xlink:href="#media-container--microphone"></use>
										</svg>
										<svg *ngIf="getTransmissionTypeClass(contact.transmission) == 'video'" class="icon">
										  <use xlink:href="#media-container--video-camera"></use>
										</svg>
										<svg *ngIf="getTransmissionTypeClass(contact.transmission) == 'webinar'" class="icon">
										  <use xlink:href="#media-container--video-lecture"></use>
										</svg>	
									</span>
								</div>
							</template>
						</p-dataList>
					</div>
				</template>
			</p-dataList>
			<button (click)="newGroup()">+ New group</button>
		</div>`
})
export class ContactListComponent implements OnInit {

	contactList: ContactList;

    constructor(
    	private router: Router,
		private authenticationService: AuthenticationService,
		private contactListService: ContactListService
	) {
		console.log("contact list - construct");
	}

	getTransmissionTypeClass(transmission: Transmission): string {
		return transmission.callType ? CallType.getIdentifier(transmission.callType) : "";
	}

	ngOnInit() {
		console.log("contact list - init()");
		this.contactListService.contactList$.subscribe(contactList => {
			this.contactList = contactList;
			console.log(this.contactList);
		});
		this.authenticationService.isLogged$.subscribe(isLogged => {
			//this.contactListService.onUpdate();
			this.contactListService.init(isLogged);
			//this.contactList = this.contactListService.contactList;
		});
	}

	newGroup() {
		this.router.navigate(["/group/new"]);
	}
}
