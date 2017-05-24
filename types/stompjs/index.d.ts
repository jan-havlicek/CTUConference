// Taken from here: https://github.com/DefinitelyTyped/DefinitelyTyped/blob/types-2.0/stompjs/index.d.ts

// Type definitions for stompjs 2.3
// Project: https://github.com/jmesnil/stomp-websocket
// Definitions by: Jimi Charalampidis <https://github.com/jimic>
// Definitions: https://github.com/DefinitelyTyped/DefinitelyTyped

export const VERSIONS: {
	V1_0: string,
	V1_1: string,
	V1_2: string,
	supportedVersions: () => Array<string>
};

export function client(url: string): Client;
export function setInterval(interval, f): number;
export function clearInterval(id: number): number;

export class Client {
	constructor(ws: WebSocket);
	ws: WebSocket;
	connected: boolean;
	counter: number;
	heartbeat: {
		incoming: number,
		outgoing: number
	};
	maxWebSocketFrameSize: number;
	subscriptions: {};

	debug(...args: string[]): any;

	connect(headers: { login: string, passcode: string, host?: string }, connectCallback: (frame?: Frame) => any, errorCallback?: (error: string) => any): any;
	connect(login: string, passcode: string, connectCallback: (frame?: Frame) => any, errorCallback?: (error: string) => any, host?: string): any;
	disconnect(disconnectCallback: () => any, headers?: {}): any;

	send(destination: string, headers?: {}, body?: string): any;
	subscribe(destination: string, callback?: (frame: Frame) => any, headers?: {}): any;
	unsubscribe(): any;

	begin(transaction: string): any;
	commit(transaction: string): any;
	abort(transaction: string): any;

	ack(messageID: string, subscription: string, headers?: {}): any;
	nack(messageID: string, subscription: string, headers?: {}): any;
}

export class Frame {
	command: string;
	headers: {};
	body: string;

	constructor(command: string, headers?: {}, body?: string);

	toString(): string;
	sizeOfUTF8(s: string): number;
	unmarshall(datas: any): any;
	marshall(command: string, headers?: {}, body?: string): any;
}

// export function client(url: string, protocols?: string | Array<string>): Client;
// export function over(ws: WebSocket): Client;
// export function overTCP(host: string, port: number): Client;
// export function overWS(url: string): Client;
// export function setInterval(interval: number, f: (...args: any[]) => void): NodeJS.Timer;
// export function clearInterval(id: NodeJS.Timer): void;
