// Taken from here and modified: https://github.com/sjmf/ng2-stompjs-demo/blob/master/src/app/services/stomp/stomp.service.ts

import {Injectable, Inject} from '@angular/core';
import {SocketMessage} from "./socket-message";
import {SocketMessageType} from "./socket-message-type";
import {TOKEN_APP_WS_SERVER} from "../app/config.token";
// import {$WebSocket} from 'angular2-websocket/angular2-websocket';
import {$WebSocket} from "../utils/angular2-websocket";
import {Subject, Observable} from "rxjs";
import {AuthenticationService} from "../authentication/authentication.service";

/**
 * This service is used to communicate with the server via WebSocket protocol.
 */
@Injectable()
export class SocketMessageService {
	/**
	 * URL of websocket server
	 */
	private url: string;

	/**
	 * Websocket connection to the server
	 */
	private ws: $WebSocket;

	/**
	 * Object, where each property is named by type of message
	 * and each property contains list of subscribers
	 */
	private subscribers: any;

	/**
	 * Array of object and each object contains type(with optional description)
	 * and promise. If there were sent more requests of one type simultaneously,
	 * both requests will be notified by the same promise created when first
	 * message was sent.
	 * If we need to create more promises for messages of the same type,
	 * you can set descriptor (path in data json), that describes property
	 * which will help to distinguish these messages.
	 * Descriptor looks like "user.address.street"
	 */
	private promiseRequests: {type: string, description?: string, promise: Promise<any>, resolve: (result) => void, timeoutID: any}[];

	private readonly REQUEST_TIMEOUT = 15*60*1000; //15min

	private _connectionFailure$: Subject<boolean>;

	private _onOpenAction = null;

	private _isOpen: boolean;

	constructor(
		@Inject(TOKEN_APP_WS_SERVER) wsServerUrl: string
	) {
		this._connectionFailure$ = <Subject<boolean>>new Subject();
		this.url = wsServerUrl;
		this.promiseRequests = [];
		this.subscribers = {};
		this.init();
	}

	/**
	 * Initializes websocket connection and registers subscribers for incoming messages
	 */
	protected init() {
		this._isOpen = false;
		this.ws = new $WebSocket(this.url);
		this.ws.connect();
		this.ws.onOpen((event) => {
			this._isOpen = true;
			console.log(`WebSocket on url ${this.url} connection opened:`, event);
			if(this._onOpenAction !== null) {
				this._onOpenAction();
				this._onOpenAction = null;
			}
		});
		this.ws.getDataStream().subscribe((result) => {
			console.log("%c Incoming message", "background: #efe; color: #0d0;", result);
			let jsonData = JSON.parse(result.data);
			let messageType = new SocketMessageType(jsonData.type);
			this.publish(messageType, jsonData);
			this.resolvePromise(messageType, jsonData);
		});
		this.ws.onClose(event => {
			this._isOpen = false;
			console.log("SOCKET CLOSED", event);
			//window.setTimeout(this.restoreSocket, 1000);
			this._connectionFailure$.next(true);
		});
		this.ws.onError(event => {
			console.log("SOCKET ERROR", event);
			//window.setTimeout(this.restoreSocket, 1000);
			this._connectionFailure$.next(true);
		});
	}

	/**
	 * Takes parsed data from data stream, finds observers, that have
	 * registered for message type taken from incoming message
	 * and performs all actions for this observers.
	 * @param messageType
	 * @param jsonData parsed data from stream
	 */
	private publish(messageType: SocketMessageType, jsonData) {
		let subscribersToNotify = this.getSubscribersToNotify(messageType);
		subscribersToNotify.forEach(subscriber => subscriber(jsonData));
	}

	/**
	 * Takes data from data stream and looks for the promise. If there is promise
	 * created for the message type (outgoing message was sent as request),
	 * promise is resolved.
	 * @param result
	 */
	private resolvePromise(messageType: SocketMessageType, jsonData) {
		let promiseRequestIndex = this.findPromiseIndex(messageType.type, jsonData.data);
		if(promiseRequestIndex === -1) return;
		let promiseRequest = this.promiseRequests[promiseRequestIndex];
		if(promiseRequest) {
			clearTimeout(promiseRequest.timeoutID);
			promiseRequest.resolve(jsonData);
			this.promiseRequests.splice(promiseRequestIndex, 1);
		}
	}

	/**
	 * Returns index of promise request, that has same message type
	 * or description, if it is set in promise request.
	 * @param messageType
	 * @param jsonData
	 * @returns {number}
	 */
	private findPromiseIndex(messageType: string, jsonData): number {
		return this.promiseRequests.findIndex(item => {
			let typeMatched = item.type === messageType;
			if(!typeMatched) return false;
			if(typeMatched && !item.description) return true;

			let [descriptor, expectedValue] = item.description.split('|');
			let [, realValue] = this.getRequestDescription(jsonData, descriptor).split('|');
			return expectedValue === realValue;
		});
	}

	/**
	 * Returns all observers, that it is necessary to notify. It depends
	 * on message type.
	 * Example: for message type "authentication.login" picks subscribers
	 * of "authentication.login", "authentication.*" and "*"
	 * @param messageType
	 */
	private getSubscribersToNotify(messageType: SocketMessageType) {
		let messageSupertype: string = messageType.superType + ".*";
		let subscribersToNotify = [];

		if(this.subscribers[messageType.type] !== undefined) {
			subscribersToNotify = subscribersToNotify.concat(this.subscribers[messageType.type]);
		}
		if(this.subscribers[messageSupertype] !== undefined) {
			subscribersToNotify = subscribersToNotify.concat(this.subscribers[messageSupertype]);
		}
		if(this.subscribers["*"] !== undefined) {
			subscribersToNotify = subscribersToNotify.concat(this.subscribers["*"]);
		}
		return subscribersToNotify;
	}

	/**
	 * Send message to the server.
	 * If the message sent is just request and there is also response required,
	 * use "request" method instead.
	 * @param {SocketMessage} message
	 */
	send(message: SocketMessage) {
		this.ws.send(message.toJSON()).subscribe((result) => {
			console.log(result);
		});
	}

	/**
	 * Registers new observer and specifies what message type it is observing.
	 * It is possible to use wildcards for mapping message type.
	 * @param messageTypePattern
	 * @param subscriber
	 */
	subscribeToDataStream(messageTypePattern: string, subscriber: (result: {type: string, message?: string, data: any}) => any) {
		if(!this.subscribers[messageTypePattern]) {
			this.subscribers[messageTypePattern] = [subscriber];
		} else {
			this.subscribers[messageTypePattern].push(subscriber);
		}
	}

	/**
	 * Send request and return promise. This enables AJAX-like functionality.
	 * It enables to send message and wait for response (simulated by message type
	 * - it waits for the first incoming message with the same message type).
	 *
	 * When more requests of the same message type is send simultaneously, it will
	 * send only the first request and all other requests are connected to the
	 * one promise.
	 *
	 * If there are sent more messages with the same type, additional descriptor
	 * can be added. It is path in json (eg. user.type). The same value in this
	 * path of json needs to be sent in response too.
	 *
	 * @param message
	 * @param descriptor
	 * @returns {Promise<any>}
	 */
	request(message: SocketMessage|string, descriptor?: string): Promise<any> {
		let messageObj = typeof message === "string" ? new SocketMessage(message) : message;
		let msgType = messageObj.type;
		let promiseRequestIndex = this.findPromiseIndex(msgType, messageObj.data);
		if(promiseRequestIndex !== -1) {
			return this.promiseRequests[promiseRequestIndex].promise;
		}
		let description = descriptor ? this.getRequestDescription(messageObj.data, descriptor) : null;
		let promise = this.createRequestPromise(msgType, description);
		this.send(messageObj);
		return promise;
	}

	/**
	 * Creates message description based on data (it is handy when it is necessary to distinguish
	 * two messages with the same type but which have different property in some json path.
	 *
	 * @param messageObj
	 * @param descriptor path in json. E.g. "user.address.street"
	 * @returns {string}
	 */
	private getRequestDescription(messageData: SocketMessage, descriptor: string) {
		let data = messageData;
		descriptor.split('.').forEach(key => {
			if(!data.hasOwnProperty(key)) {
				return `${descriptor}|-`;
			};
			data = data[key];
		})
		return `${descriptor}|${data}`;
	}

	/**
	 * Creates promise for given message type
	 * @param {string} msgType
	 * @returns {Promise<any>}
	 */
	private createRequestPromise(msgType: string, description?: string): Promise<any> {
		let timeoutID = null, resolver = () => {};
		let promise = new Promise<any>((resolve, reject) => {
			resolver = resolve;
			timeoutID = setTimeout(() => {
				reject("Request timeout! Message type: "+msgType)
			}, this.REQUEST_TIMEOUT);
		});
		let promiseRequest = {type: msgType, description: description, promise: promise, resolve: resolver, timeoutID: timeoutID};
		this.promiseRequests.push(promiseRequest);
		return promise;
	}

	/**
	 * Returns observable of failures of connection.
	 * @returns {Observable<boolean>}
	 */
	get connectionFailure$(): Observable<boolean> {
		return this._connectionFailure$.asObservable();
	}

	set onOpenAction(action) {
		this._onOpenAction = action;
	}

	get isConnectionOpen(): boolean {
		return this._isOpen;
	}
}
