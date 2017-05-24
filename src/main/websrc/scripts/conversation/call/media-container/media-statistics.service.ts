import {Injectable} from "@angular/core";
import WebRtcPeer = kurentoUtils.WebRtcPeer.WebRtcPeer;

@Injectable()
export class MediaStatisticsService {

	private _statsGatherInterval = null;

	private _lastBytesSent = 0;

	public setStatistics(webRtcEndpoint: WebRtcPeer) {
		webRtcEndpoint.on('MediaStateChanged', function(event) {
			if (event.newState == "CONNECTED") {
				console.log("MediaState is CONNECTED ... printing stats...")
				this.activateStats(webRtcEndpoint);
			}
		});
	}

	/**
	 * Enable gathering of statistic for defined WebRTC endpoint. The gathering
	 * is performed in the frequency defined by interval.
	 * @param webRtcEndpoint
	 * @param interval Interval between two statistics gatherings (in seconds).
	 */
	public activateStats(webRtcEndpoint: WebRtcPeer, interval) {
		this._statsGatherInterval = setInterval(() => {
			this.getBrowserOutgoingVideoStats(webRtcEndpoint, (a, stats) => {
				console.log("STATS: ", stats);
			}, interval, this);
		}, interval * 1000);
	}

	public closeStatistics() {
		clearInterval(this._statsGatherInterval);
	}

	private getBrowserOutgoingVideoStats(webRtcPeer, callback, timeInterval, this__) {
		var peerConnection = webRtcPeer.peerConnection;
		var localVideoTrack = peerConnection.getLocalStreams()[0].getVideoTracks()[0];

		peerConnection.getStats(function(stats) {
			var results = stats.result();

			for (var i = 0; i < results.length; i++) {
				var res = results[i];
				if (res.type != 'ssrc') continue;
				console.log("Stream i: ", i);
				//Publish it to be compliant with W3C stats draft
				var retVal = {
					timeStamp: res.timestamp,
					//StreamStats below
					associateStatsId: res.id,
					codecId: "--",
					firCount: res.stat('googFirsReceived'),
					isRemote: false,
					mediaTrackId: res.stat('googTrackId'),
					nackCount: res.stat('googNacksReceived'),
					pliCount: res.stat('googPlisReceived'),
					sliCount: 0,
					ssrc: res.stat('ssrc'),
					transportId: res.stat('transportId'),
					//Specific outbound below
					bytesSent: res.stat('bytesSent'),
					packetsSent: res.stat('packetsSent'),
					roundTripTime: res.stat('googRtt'),
					packetsLost: res.stat('packetsLost'),
					targetBitrate: res.stat('targetBitrate'),
					currentBitrate: (res.stat('bytesSent') - this__._lastBytesSent) / timeInterval,
					remb: res.stat('remb')
				}
				this__._lastBytesSent = res.stat('bytesSent');
				return callback(null, retVal);
			}
			return callback("Error: could not find ssrc type on track stats", null);
		}, localVideoTrack);
	}
}
