export class ParticipantMedia {

	private _id: number;
	private _rtcPeer: kurentoUtils.WebRtcPeer.WebRtcPeerSendonly;

	constructor(id: number, rtcPeer) {
		this._id = id;
		this._rtcPeer = rtcPeer;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get rtcPeer(): kurentoUtils.WebRtcPeer.WebRtcPeerSendonly {
		return this._rtcPeer;
	}

	set rtcPeer(value: kurentoUtils.WebRtcPeer.WebRtcPeerSendonly) {
		this._rtcPeer = value;
	}
}
