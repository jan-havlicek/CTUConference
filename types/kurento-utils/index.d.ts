declare module kurentoUtils {

	export namespace WebRtcPeer {

		abstract class WebRtcPeer {
			constructor(options: any, callable: any);
			generateOffer(callable: (error, offerSdp, wp) => void);
			processAnswer(sdpAnswer, callable: (error) => void);
			addIceCandidate(candidate, callable: (error) => void);
			dispose();
			getLocalStream(index?: number): any;
			getRemoteStream(index?: number): any;
			on(eventName: string, callback: (event) => void): void;
		}

		export class WebRtcPeerSendrecv extends WebRtcPeer{}

		export class WebRtcPeerSendonly extends WebRtcPeer{}

		export class WebRtcPeerRecvonly extends WebRtcPeer{}
	}

}

