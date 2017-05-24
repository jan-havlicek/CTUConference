import {Component} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Group} from "./group";
import {GroupService} from "./group.service";
import {GroupType} from "./group-type.enum";
@Component({
	selector: 'group-form',
	template: `<div class="simple-container group-form">
<h2>Create new group</h2>
		<form novalidate (ngSubmit)="createGroup()"
			[formGroup]="groupForm">
			<div>
				<label for="name">Name</label>
				<input type="text" formControlName="name" class="name" required>
				<div *ngIf="groupForm.controls.name.dirty && !groupForm.controls.name.valid">
				   <p *ngIf="groupForm.controls.name.errors.required" class="error-msg">
					  Name is required
				   </p>
				</div>
			</div>
			<div>
				<label for="groupType">Group type</label>
				<p-dropdown [options]="groupTypeOptions" formControlName="groupType"></p-dropdown>
			</div>
			<div>
				<button type="submit" [disabled]="!groupForm.valid">Create group</button>
			</div>
		</form>
</div>`
})
export class GroupFormComponent {

	groupForm: FormGroup;

	group: Group;

	groupTypeOptions: {label: string, value: any}[];

	constructor(
		private formBuilder: FormBuilder,
		private groupService: GroupService
	) {
		console.log("Group form - construct");
		this.groupTypeOptions = GroupType.getOptionList();
	}

	ngOnInit() {
		console.log("Group form - init()");
		this.groupForm = this.formBuilder.group({
			name: ['', [Validators.required, Validators.minLength(3)]],
			groupType: ['', Validators.required],
		});
	}

	createGroup() {
		let groupFormData = this.groupForm.getRawValue();
		let group = new Group();
		group.name = groupFormData.name;
		group.groupType = GroupType.get(groupFormData.groupType);
		this.groupService.createGroup(group);
	}
}
