import {ParticipantInfoInterface} from "./participant-info.interface";
import {ParticipantType} from "./participant-type.enum";
/**
 * This class describes data about participants, that are part of
 * media transmission during the audio/voice call.
 * This data are sent for every participant everytime, some participant
 * state changes.
 *
 * It says whether user has muted audio or video
 *
 * User has muted both audio and video when they are joined
 * as listener of webinar. When he raise the hand, audio or video
 * can be transmitted.
 *
 * When video call is performed, there should be possible to mute
 * the video or voice transmission
 *
 * User has muted only video when the audio call is performed.
 */
export class ParticipantInfo implements ParticipantInfoInterface {
	private _id: number;
	private _videoMuted: boolean; //if video transmission from participant is muted
	private _audioMuted: boolean; //if audio transmission from participant is muted
	private _isInitiator: boolean; //the initiator of the call
	private _participantType: ParticipantType;

	/**
	 * This is not sent between client and server. It is local
	 * information about whether the stream for the participant
	 * was initialized or not
	 */
	private _hasMedia: boolean;

	static fromJson(jsonObject): ParticipantInfo {
		let participantInfo = new ParticipantInfo();
		participantInfo.id = jsonObject.id;
		participantInfo.videoMuted = jsonObject.videoMuted;
		participantInfo.audioMuted = jsonObject.audioMuted;
		participantInfo.isInitiator = (jsonObject.isInitiator) ? jsonObject.isInitiator : false;
		participantInfo._participantType = ParticipantType.get(jsonObject.participantType);
		participantInfo.hasMedia = false;
		return participantInfo;
	}

	/**
	 * Get the info, whether the user is transmitting audio or video. If not, it is only listener.
	 * @returns {boolean}
	 */
	get isTransmitting(): boolean {
		return !this._videoMuted || !this._audioMuted;
	}

	get id(): number {
		return this._id;
	}

	set id(value: number) {
		this._id = value;
	}

	get videoMuted(): boolean {
		return this._videoMuted;
	}

	set videoMuted(value: boolean) {
		this._videoMuted = value;
	}

	get audioMuted(): boolean {
		return this._audioMuted;
	}

	set audioMuted(value: boolean) {
		this._audioMuted = value;
	}

	get hasMedia(): boolean {
		return this._hasMedia;
	}

	set hasMedia(value: boolean) {
		this._hasMedia = value;
	}
	get isInitiator(): boolean {
		return this._isInitiator;
	}

	set isInitiator(value: boolean) {
		this._isInitiator = value;
	}
	get participantType(): ParticipantType {
		return this._participantType;
	}

	set participantType(value: ParticipantType) {
		this._participantType = value;
	}
}
