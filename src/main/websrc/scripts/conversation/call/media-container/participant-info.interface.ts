import {ParticipantType} from "./participant-type.enum";

export interface ParticipantInfoInterface {
	id: number;
	videoMuted: boolean;
	audioMuted: boolean;
	isInitiator: boolean;
	participantType: ParticipantType;
}
