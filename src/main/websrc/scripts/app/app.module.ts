import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import { AppComponent }   from './app.component';
import { routing,
         appRoutingProviders } from './app.routing';

import { AuthenticationService } from '../authentication/authentication.service';
import { LoggedInGuard } from '../authentication/logged-in.guard';
import { LoggedOutGuard } from '../authentication/logged-out.guard';

import { LoginComponent } from '../authentication/login.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { ConversationComponent }  from '../conversation/conversation.component';
import { SettingsComponent }  from '../settings/settings.component';

import { ContactListComponent } from '../contact/contact-list/contact-list.component';

import {MessagesModule} from "primeng/primeng";
import {MenubarModule} from "primeng/primeng";
import {DataListModule} from "primeng/components/datalist/datalist";
import {OverlayPanelModule} from "primeng/components/overlaypanel/overlaypanel";
import {ChatComponent} from "../conversation/chat/chat.component";
import {ContactListService} from "../contact/contact-list/contact-list.service";
import {GroupComponent} from "../contact/group/group.component";
import {FriendComponent} from "../contact/friend/friend.component";
import {GroupEventComponent} from "../contact/group/group-event.component";
import {GroupTopicComponent} from "../contact/group/group-topic.component";
import {MultichatComponent} from "../contact/friend/multichat.component";
import {GroupService} from "../contact/group/group.service";
import {FriendService} from "../contact/friend/friend.service";
import {HomepageComponent} from "../homepage/homepage.component";
import {RegistrationComponent} from "../registration/registration.component";
import {RegistrationService} from "../registration/registration.service";
import {ChatService} from "../conversation/chat/chat.service";
import {CallService} from "../conversation/call/call.service";
import {CallNotifierComponent} from "../conversation/call/call-notifier/call-notifier.component";
import {DialogModule} from "primeng/components/dialog/dialog";
import {CallContainerComponent} from "../conversation/call/call-container/call-container.component";
import {MediaService} from "../conversation/call/media-container/media.service";
import {TOKEN_APP_WS_SERVER} from "./config.token";
import {SocketMessageService} from "../communication/socket-message.service";
import {MediaContainerComponent} from "../conversation/call/media-container/media-container.component";
import {ParticipantMediaComponent} from "../conversation/call/media-container/participant-media.component";
import {InfoComponent} from "../conversation/info.component";
import {PanelModule} from "primeng/components/panel/panel";
import {LoginFormComponent} from "../authentication/login-form.component";
import {SpeechDetectionService} from "../conversation/call/media-container/speech-detection.service";
import {SuggestedFriendsComponent} from "../contact/friend/suggested-friends.component";
import {SuggestedGroupsComponent} from "../contact/group/suggested-groups.component";
import {CarouselModule} from "primeng/components/carousel/carousel";
import {OngoingCallsComponent} from "../conversation/call/ongoing-calls.component";
import {PdfViewerComponent} from "ng2-pdf-viewer";
import {HandoutsComponent} from "../conversation/handouts/handouts.component";
import {HandoutsLoaderComponent} from "../conversation/handouts/handouts-loader.component";
import {HandoutsService} from "../conversation/handouts/handouts.service";
import {FileService} from "../conversation/attachment/file.service";
import {AttachmentService} from "../conversation/attachment/attachment.service";
import {ApiService} from "../utils/api.service";
import {STOMPService} from "../communication/ng2-stompjs-demo/stomp.service";
import {AttachmentComponent} from "../conversation/attachment/attachment.component";
import {AttachmentPickerComponent} from "../conversation/attachment/attachment-picker.component";
import {APP_BASE_HREF} from "@angular/common";
import {FlashMessageService} from "./flash-message.service";
import {NotificationService} from "../notification/notification.service";
import {NotificationComponent} from "../notification/notification.component";
import {GroupFormComponent} from "../contact/group/group-form.component";
import {NotificationConverter} from "../notification/notification.converter";
import {ContactSearchComponent} from "../contact/contact-search/contact-search.component";
import {GrowlModule} from "primeng/components/growl/growl";
import {DataTableModule} from "primeng/components/datatable/datatable";
import {DropdownModule} from "primeng/components/dropdown/dropdown";
import {ContactService} from "../contact/contact-search/contact.service";
import {AppService} from "./app.service";
import {MediaStatisticsService} from "../conversation/call/media-container/media-statistics.service";


const APP_WS_SERVER_URL = "wss://" + location.host + "/CTUConference/app";

@NgModule({
    imports:      [
        BrowserModule,
        routing,
        HttpModule,
        FormsModule,
		ReactiveFormsModule,
		MessagesModule,
		GrowlModule,
		MenubarModule,
		DataListModule,
		DataTableModule,
		OverlayPanelModule,
		PanelModule,
		DialogModule,
		CarouselModule,
		DropdownModule
	],
    declarations: [
        AppComponent,
		HomepageComponent,
        DashboardComponent,
        LoginComponent,
		LoginFormComponent,
        ConversationComponent,
        SettingsComponent,
        ContactListComponent,
		ChatComponent,
		InfoComponent,
		FriendComponent,
		MultichatComponent,
		GroupComponent,
		GroupEventComponent,
		GroupTopicComponent,
		RegistrationComponent,
		CallNotifierComponent,
		CallContainerComponent,
		MediaContainerComponent,
		ParticipantMediaComponent,
		SuggestedFriendsComponent,
		SuggestedGroupsComponent,
		OngoingCallsComponent,
		PdfViewerComponent,
		HandoutsLoaderComponent,
		HandoutsComponent,
		AttachmentPickerComponent,
		AttachmentComponent,
		NotificationComponent,
		GroupFormComponent,
		ContactSearchComponent
    ],
    providers: [
        appRoutingProviders, AuthenticationService, SocketMessageService,
		ApiService, STOMPService, AppService,
		LoggedInGuard, LoggedOutGuard, RegistrationService,
		ContactListService, ContactService, FriendService, GroupService,
		ChatService, CallService, MediaService, SpeechDetectionService,
		MediaStatisticsService, AttachmentService, FileService, HandoutsService,
		FlashMessageService, NotificationService, NotificationConverter,
		{ provide: TOKEN_APP_WS_SERVER, useValue: APP_WS_SERVER_URL },
		{provide: APP_BASE_HREF, useValue : '/CTUConference/' }
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
