import {Injectable} from "@angular/core";
//import hark = require("hark");
import {Subject} from "rxjs";
import * as hark from "hark";
import {ParticipantSpeech} from "./participant-speech";
import {SocketMessageService} from "../../../communication/socket-message.service";
import {SocketMessage} from "../../../communication/socket-message";

@Injectable()
export class SpeechDetectionService {

	private _speech;
	private _myId: number;

	private _speechDetection$: Subject<ParticipantSpeech>;

	constructor(
		private messageService: SocketMessageService
	) {
		this._speechDetection$ = <Subject<ParticipantSpeech>>new Subject();
		this.messageService.subscribeToDataStream("speech.speaking", (result) => {
			this._speechDetection$.next(result.data);
		});
	}

	init(myId: number) {
		this._myId = myId;
	}

	/**
	 * Set a listener to the stream. It will test in some interval
	 * whether the stream contains speech or not. It then emits observable
	 * for each event of start and stop speaking.
	 * @param rtcPeer
	 */
	listenSpeech(stream) {
		let options = {
			interval: 300
		};
		this._speech = hark(stream, options);
		this._speech.on("speaking", () => this.broadcastSpeechInfo(true));
		this._speech.on("stopped_speaking", () => this.broadcastSpeechInfo(false));
	}

	/**
	 * Broadcast information about if I am speaking or not
	 * @param isSpeaking
	 */
	private broadcastSpeechInfo(isSpeaking: boolean) {
		this.messageService.send(
			new SocketMessage("speech.speaking", {
				isSpeaking: isSpeaking
			})
		);
	}

	/**
	 * Removes listener of the stream
	 */
	stopListening() {
		this._speech.stop();
		this._speech = null;
	}

	/**
	 * Returns observable, which returns boolean value when the speaking state
	 * is changed.
	 * @returns {Observable<boolean>}
	 */
	getSpeechDetection$(participantId: number) {
		return this._speechDetection$
			.filter(item => item.participantId == participantId)
			.map(item => item.isSpeaking);
	}
}
