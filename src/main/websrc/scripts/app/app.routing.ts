import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { LoginComponent } from '../authentication/login.component';

import { DashboardComponent } from '../dashboard/dashboard.component';
import { SettingsComponent }  from '../settings/settings.component';

import { LoggedInGuard } from '../authentication/logged-in.guard';
import { LoggedOutGuard } from '../authentication/logged-out.guard';
import {FriendComponent} from "../contact/friend/friend.component";
import {GroupComponent} from "../contact/group/group.component";
import {HomepageComponent} from "../homepage/homepage.component";
import {GroupFormComponent} from "../contact/group/group-form.component";

const appRoutes: Routes = [
	{ path: '', component: HomepageComponent, pathMatch: 'full', canActivate: [LoggedOutGuard] },
	{ path: 'login', component: LoginComponent, pathMatch: 'full', canActivate: [LoggedOutGuard] },
    { path: 'dashboard', component: DashboardComponent, canActivate: [LoggedInGuard] },
	{ path: 'friend/:id', component: FriendComponent, canActivate: [LoggedInGuard] },
	{ path: 'friend/:id/:view', component: FriendComponent, canActivate: [LoggedInGuard] },
	{ path: 'group/new', component: GroupFormComponent, canActivate: [LoggedInGuard] },
	{ path: 'group/:id', component: GroupComponent, canActivate: [LoggedInGuard] },
	{ path: 'group/:id/:view', component: GroupComponent, canActivate: [LoggedInGuard] }
];

export const appRoutingProviders: any[] = [

];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);
