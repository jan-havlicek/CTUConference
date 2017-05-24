import {Injectable} from "@angular/core";
import {SocketMessage} from "../../communication/socket-message";
import {Subject, Observable, BehaviorSubject} from "rxjs";
import {CallInfo} from "./call-info";
import {CallType} from "./call-type.enum";
import {AuthenticationService} from "../../authentication/authentication.service";
import {SocketMessageService} from "../../communication/socket-message.service";
import {Router} from "@angular/router";
import {ContactListService} from "../../contact/contact-list/contact-list.service";
import {Group} from "../../contact/group/group";

@Injectable()
export class CallService {

	private _callInvocation$: Subject<CallInfo>;
	private _callStarting$: Subject<number>;
	private _callTermination$: Subject<number>;
	private _callTransmitting$: Subject<boolean>;

	private _isTransmitting: boolean = false;
	private _callInfo: CallInfo;

	constructor (
		private authenticationService: AuthenticationService,
		private router: Router,
		private contactListService: ContactListService,
		private messageService: SocketMessageService
	) {
		this._callInvocation$ = <Subject<CallInfo>>new Subject();
		this._callStarting$ = <Subject<number>>new Subject()
		this._callTermination$ = <Subject<number>>new Subject();
		this._callTransmitting$ = <Subject<boolean>>new BehaviorSubject(false);
	}

	init() {
		this.messageService.subscribeToDataStream("call.*", (request) => {
			let data = request.data;
			if(request.type === "call.request") {
				this.onCallRequest(request.data);
			} else if(request.type === "call.terminated") {
				this.onCallTerminated(request.data);
			} else if(request.type === "call.started") {
				this.onCallStarted(request.data);
			}
		});
	}

	private onCallRequest(data) {
		let callInfo = CallInfo.fromJson(data);
		this._callInfo = callInfo;
		this._isTransmitting = true;
		this._callTransmitting$.next(this._isTransmitting);
		this._callInvocation$.next(callInfo);
	}

	private onCallTerminated(data) {
		let conversationId = data.conversationId;
		this._isTransmitting = false;
		this._callTransmitting$.next(this._isTransmitting);
		this._callTermination$.next(conversationId);
	}

	private onCallStarted(data) {
		let conversationId = data.conversationId;
		this._isTransmitting = true;
		this._callTransmitting$.next(this._isTransmitting);
		this._callStarting$.next(conversationId);
	}

	/**
	 * Invoke voice call. Users can communicate only via microphone
	 * @param conversationId
	 */
	invokeVoiceCall(conversationId: number) {
		this._invokeCall(conversationId, CallType.Voice);
	}

	/**
	 * Invoke video call. All participants can send and receive video stream
	 * @param conversationId
	 */
	invokeVideoCall(conversationId: number) {
		this._invokeCall(conversationId, CallType.Video);
	}

	/**
	 * Invoke video call. Only the invoker can send video stream,
	 * all other participants can only receive video stream
	 * @param conversationId
	 */
	invokeWebinar(conversationId: number) {
		this._invokeCall(conversationId, CallType.Webinar);
	}

	/**
	 * Invoke call by me
	 * @param callType
	 * @param conversationId
	 */
	private _invokeCall(conversationId: number, callType: CallType) {
		var data = {
			conversationId: conversationId,
			callType: CallType.getIdentifier(callType)
		};
		this.messageService.send(new SocketMessage("call.invoke", data));

		let user = this.authenticationService.authUser;
		let callName = "DUMMY"; //@todo get name of conversation or better remove
		let callInfo = new CallInfo(conversationId, user.id, user.fullName, callName, callType, true);
		this._callInfo = callInfo;
		this._isTransmitting = true;
		this._callTransmitting$.next(this._isTransmitting);
		this._callInvocation$.next(callInfo);
	}

	/**
	 * Cancel call invoked by me (when call is still waiting)
	 * @param conversationId
	 */
	cancelCall (conversationId: number) {
		this._isTransmitting = false;
		this._callTransmitting$.next(this._isTransmitting);
		this._callTermination$.next(conversationId);
		this.messageService.send(new SocketMessage("call.cancel", {conversationId: conversationId}));
	}

	/**
	 * Leave call
	 * @param conversationId
	 */
	leaveCall () {
		this._isTransmitting = false;
		this._callTransmitting$.next(this._isTransmitting);
		this._callTermination$.next(this.callInfo.conversationId);
		this.messageService.send(new SocketMessage("call.leave", {}));
	}

	/**
	 * Accept incoming call and show conversation belonging to this call.
	 */
	acceptCall (conversationId: number, callInfo?: CallInfo) {
		if(callInfo) this._callInfo = callInfo;
		this._isTransmitting = true;
		this._callTransmitting$.next(this._isTransmitting);
		this._callStarting$.next(conversationId);
		this.messageService.send(new SocketMessage("call.accept", {conversationId: conversationId}));
		this.navigateToChat(conversationId);
	}

	/**
	 * Navigate to chat of the contact, that has this conversation id.
	 * @param conversationId
	 */
	navigateToChat(conversationId: number) {
		let contact = this.contactListService.findContact(conversationId);
		let contactType = contact instanceof Group ? "group" : "friend";
		this.router.navigate([`/${contactType}/${contact.id}/chat`]);
	}

	/**
	 * Reject incoming call
	 */
	rejectCall (conversationId: number) {
		this._isTransmitting = false;
		this._callTransmitting$.next(this._isTransmitting);
		this._callTermination$.next(conversationId);
		this.messageService.send(new SocketMessage("call.reject", {conversationId: conversationId}));
	}

	get callInvocation$(): Observable<CallInfo> {
		return this._callInvocation$.asObservable();
	}

	get callTermination$(): Observable<number> {
		return this._callTermination$.asObservable();
	}

	get callStarting$(): Observable<number> {
		return this._callStarting$.asObservable();
	}

	get isTransmitting(): boolean {
		return this._isTransmitting;
	}

	get isTransmitting$(): Observable<boolean> {
		return this._callTransmitting$.asObservable();
	}

	get callInfo(): CallInfo {
		return this._callInfo;
	}

}
