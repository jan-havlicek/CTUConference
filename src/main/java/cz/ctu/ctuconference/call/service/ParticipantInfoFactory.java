package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.call.domain.ParticipantType;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;
import cz.ctu.ctuconference.call.dto.ParticipantInfoDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Nick nemame on 06.11.2016.
 */
@Component
public class ParticipantInfoFactory {

	public ParticipantInfo create(long userId, CallType callType, boolean isInitiator) {
		if(callType == CallType.WEBINAR) {
			if(isInitiator) {
				return new ParticipantInfo(userId, false, false, true, ParticipantType.LECTOR);
			} else {
				return new ParticipantInfo(userId, true, true, false, ParticipantType.LISTENER);
			}
		} else if(callType == CallType.VIDEO_CALL) {
			return new ParticipantInfo(userId, false, false, isInitiator, ParticipantType.CALLING_MEMBER);
		} else {
			return new ParticipantInfo(userId, false, true, isInitiator, ParticipantType.CALLING_MEMBER);
		}
	}
//
//	public ParticipantInfoDTO create(long participantId, CallInfoDTO conversationInfo) {
//		if(conversationInfo.getCallType().equals(CallType.VOICE_CALL)) {
//			return createAsAudioParticipant(participantId);
//		} else if(conversationInfo.getCallType().equals(CallType.VIDEO_CALL)) {
//			return createAsVideoParticipant(participantId);
//		} else {
//			if(conversationInfo.getInitiatorId() == participantId) {
//				return createAsWebinarParticipant(participantId);
//			} else {
//				return createAsWebinarModerator(participantId);
//			}
//		}
//	}
//
//	/**
//	 * Creates participant with just audio enabled. It is designated for voice call
//	 * @param id participant id
//	 * @return
//	 */
//	private ParticipantInfoDTO createAsAudioParticipant(long id) {
//		return new ParticipantInfoDTO(id, false, true);
//	}
//
//	/**
//	 * Creates participant with audio and video enabled. It is designated for video calls
//	 * @param id participant id
//	 * @return
//	 */
//	private ParticipantInfoDTO createAsVideoParticipant(long id) {
//		return new ParticipantInfoDTO(id, false, false);
//	}
//
//	/**
//	 * Creates participant that can send neither audio nor video - it is designated for listeners only
//	 * @param id
//	 * @return
//	 */
//	private ParticipantInfoDTO createAsWebinarParticipant(long id) {
//		return new ParticipantInfoDTO(id, false, false);
//	}
//
//	/**
//	 * Creates webinar moderator, which the only participant, that can broadcast their audio and video stream
//	 * @param id
//	 * @return
//	 */
//	private ParticipantInfoDTO createAsWebinarModerator(long id) {
//		return new ParticipantInfoDTO(id, true, true, true);
//	}
//

}
