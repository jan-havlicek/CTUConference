
import {Injectable} from "@angular/core";
import {Subject, Observable} from "rxjs";
import {SocketMessageService} from "../../../communication/socket-message.service";
import {ParticipantInfo} from "./participant-info";
import {ParticipantMedia} from "./participant-media";
import {SocketMessage} from "../../../communication/socket-message";
import {ParticipantInfoInterface} from "./participant-info.interface";
import WebRtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeer;
import {MediaStatisticsService} from "./media-statistics.service";

@Injectable()
export class MediaService {

	private STATS_ENABLED = false;

	private _participantList: ParticipantInfo[];
	private _participantMediaList: ParticipantMedia[];

	private _me: ParticipantInfo;
	private _myMedia: ParticipantMedia;

	private _participantList$: Subject<ParticipantInfo[]>;
	private _myInfo$: Subject<ParticipantInfo>;

	constructor(
		private messageService: SocketMessageService,
		private mediaStatisticsService: MediaStatisticsService
	) {
		this._participantList$ = <Subject<ParticipantInfo[]>>new Subject();
		this._myInfo$ = <Subject<ParticipantInfo>>new Subject();
		this._participantList = [];
		this._participantMediaList = [];

		this.messageService.subscribeToDataStream("call.*", (request) => {
			if(request.type === "call.existingParticipants") {
				this.onExistingParticipants(request.data);
			} else if(request.type === "call.newParticipantArrived") {
				this.onNewParticipantArrived(request.data);
			} else if(request.type === "call.participantLeft") {
				this.onParticipantLeft(request.data);
			} else if(request.type === "call.participantTransmissionChanged") {
				this.onParticipantTransmissionChanged(request.data);
			} else if(request.type === "call.receiveVideoAnswer") {
				this.onReceiveVideoAnswer(request.data);
			} else if(request.type === "call.iceCandidate") {
				this.onIceCandidate(request.data);
			} else if(request.type === "call.participantChanged") {
				this.participantChanged(request.data);
			}
		});
	}

	/**
	 *
	 * @param {{myInfo: ParticipantInfoInterface, participantList: [ParticipantInfoInterface]}} data
	 */
	private onExistingParticipants(data: {myInfo: ParticipantInfoInterface, participantInfoList: [ParticipantInfoInterface]}) {
		this._participantList = data.participantInfoList.map(
			item => ParticipantInfo.fromJson(item));
		this._me = ParticipantInfo.fromJson(data.myInfo);

		this._participantList$.next(this._participantList);
		this._myInfo$.next(this._me);
	}

	/**
	 *
	 * @param {ParticipantInfoInterface} data
	 */
	private onNewParticipantArrived(data: ParticipantInfoInterface) {
		let participant = ParticipantInfo.fromJson(data);
		this._participantList.push(participant);

		this._participantList$.next(this._participantList);
	}

	private onParticipantLeft(data: {participantId: number}) {
		let participantId = data.participantId;
		let listIndex = this._participantList.findIndex(item => item.id === participantId);
		this._participantList.splice(listIndex, 1);
	}

	private onParticipantTransmissionChanged(data) {
		let participant = ParticipantInfo.fromJson(data.participant);
	}

	private getParticipantMedia(participantId: number): ParticipantMedia {
		let participantMedia;
		if(participantId === this._me.id) {
			participantMedia = this._myMedia;
		} else {
			participantMedia = this._participantMediaList.find(item => item.id === participantId);
		}
		return participantMedia;
	}

	private onReceiveVideoAnswer(data: {participantId: number, sdpAnswer: string}) {
		let participantMedia = this.getParticipantMedia(data.participantId);
		participantMedia.rtcPeer.processAnswer(data.sdpAnswer, function (error) {
			if (error) return console.error (error);
		});
	}

	private onIceCandidate(data: {participantId: number, candidate: any}) {
		let participantMedia = this.getParticipantMedia(data.participantId);
		participantMedia.rtcPeer.addIceCandidate(data.candidate, function (error) {
			if (error) {
				console.error("Error adding candidate: " + error);
				return;
			}
		});
	}

	setMyStream(mediaElement): Promise<any> {
		let iceCandidateFunctionDependencies = {
			messageService: this.messageService,
			participantId: this._me.id
		};
		var mediaConstraints = this.getMediaConstraints(this._me);
		var options = {
			localVideo: mediaElement,
			mediaConstraints: mediaConstraints,
			onicecandidate: onIceCandidate.bind(iceCandidateFunctionDependencies)
		};
		let this__ = this; //hack - "this" should be WebRtcPeer, but wee need to access this class properties too
		let streamPromise = new Promise<any>((resolve, reject) => {
			let rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(options,
				function (error) {
					if(error) {
						console.error(error);
						reject(error);
					}
					let offerFunctionDependencies = {
						messageService: this__.messageService,
						participant: this__._me
					};
					this.generateOffer(offerToReceiveVideo.bind(offerFunctionDependencies));
					if(this__.STATS_ENABLED) {
						this__.mediaStatisticsService.activateStats(rtcPeer, 1);
					}
					resolve(rtcPeer.getLocalStream());
				});
			this._myMedia = new ParticipantMedia(this._me.id, rtcPeer);
			this._me.hasMedia = true;
		});
		return streamPromise;
	}

	private getMediaConstraints(participantInfo: ParticipantInfo) {
		var constraints = {
			audio : !participantInfo.audioMuted,
			video : participantInfo.videoMuted ? false : {
				mandatory : {
					maxWidth : 320,
					maxFrameRate : 30,
					minFrameRate : 30
				}
			}
		};
		return constraints;
	}

	/**
	 *
	 * @param participant
	 * @param mediaElement
	 */
	setParticipantStream(participant: ParticipantInfo, mediaElement) {
		let iceCandidateFunctionDependencies = {
			messageService: this.messageService,
			participantId: participant.id
		};
		var options = {
			remoteVideo: mediaElement,
			onicecandidate: onIceCandidate.bind(iceCandidateFunctionDependencies)
		}
		let this__ = this; //hack - this should e WebRtcPeer, but wee need to access this class properties
		let rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(options,
			function (error) {
				if(error) {
					return console.error(error);
				}
				let offerFunctionDependencies = {
					messageService: this__.messageService,
					participant: participant
				};
				this.generateOffer(offerToReceiveVideo.bind(offerFunctionDependencies));
			});
		let participantMedia = new ParticipantMedia(participant.id, rtcPeer);
		this._participantMediaList.push(participantMedia);
		participant.hasMedia = true;
	}

	closeParticipantStream(participant: ParticipantInfo) {
		let listIndex = this._participantMediaList.findIndex(item => item.id === participant.id);
		this._participantMediaList[listIndex].rtcPeer.dispose();
		this._participantMediaList.splice(listIndex, 1);
	}

	closeMyStream() {
		if(this.STATS_ENABLED) {
			this.mediaStatisticsService.closeStatistics();
		}
		this._myMedia.rtcPeer.dispose();
	}

	getMyStream(): kurentoUtils.WebRtcPeer.WebRtcPeerSendonly {
		return this._myMedia.rtcPeer;
	}

	get participantList$(): Observable<ParticipantInfo[]> {
		return this._participantList$.asObservable();
	}

	get myInfo$(): Observable<ParticipantInfo> {
		return this._myInfo$.asObservable();
	}

	private participantChanged(data: any) {

	}
}


function offerToReceiveVideo (this: {messageService: SocketMessageService; participant: ParticipantInfo}, error, offerSdp, wp){
	if (error) return console.error ("sdp offer error");
	console.log('Invoking SDP offer callback function');
	var data =  {
		senderId: this.participant.id,
		sdpOffer: offerSdp
	};
	let sdpOfferMsg = new SocketMessage("call.receiveVideoFrom", data);
	this.messageService.send(sdpOfferMsg);
}

/**
 *
 * @param candidate
 * @param wp
 */
function onIceCandidate (this: {messageService: SocketMessageService, participantId: number}, candidate, wp) {
	console.log("Local candidate" + JSON.stringify(candidate));
	let data = {
		candidate: candidate,
		participantId: this.participantId
	};
	this.messageService.send(new SocketMessage("call.onIceCandidate", data));
}
