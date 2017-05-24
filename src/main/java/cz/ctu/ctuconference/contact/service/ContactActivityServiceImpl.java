package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.service.CallConverter;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Nick Nemame on 25.09.2016.
 */
@Service
public class ContactActivityServiceImpl implements ContactActivityService {

	@Autowired
	private UserRegistry userRegistry;

	@Autowired
	CallManager callManager;

	@Autowired
	CallConverter callConverter;

	public void augmentActivityInfo(ConversationEntityDTO conversationEntity, boolean augmentConversationState) {
		augmentParticipantActivityInfo(conversationEntity);
		if(augmentConversationState) {
			augmentConversationActivityInfo(conversationEntity);
		}
	}

	private void augmentParticipantActivityInfo(ConversationEntityDTO conversationEntity) {
		conversationEntity.getConversation().getParticipantList().forEach((participantDTO) -> {
			UserSession userSession = userRegistry.getByUserId(participantDTO.getId());
			boolean online, transmitting, doNotDisturb;
			if(userSession != null) {
				online = true;
				if(userSession.isInCall()) {
					if(userSession.getUserCall().getConversationId() == conversationEntity.getConversation().getId()) {
						transmitting = true;
						doNotDisturb = false;
					} else {
						transmitting = false;
						doNotDisturb = true;
					}
				} else {
					transmitting = false;
					doNotDisturb = false;
				}
			} else {
				online = transmitting = doNotDisturb = false;
			}
			participantDTO.setOnline(online);
			participantDTO.setTransmitting(transmitting);
			participantDTO.setDoNotDisturb(doNotDisturb);
		});
	}

	/**
	 * Set Call state and call type.
	 * If there is no call, call state will be NONE and call type will be empty.
	 * Otherwise both values will be set.
	 * @param conversationEntity
	 */
	private void augmentConversationActivityInfo(ConversationEntityDTO conversationEntity) {
		long conversationId = conversationEntity.getConversation().getId();
		CallState callState = CallState.NONE;
		Call call = callManager.getCall(conversationId);
		if(call != null) {
			CallType callType = call.getCallType();
			if(call.isRequesting()) callState = CallState.REQUESTING;
			if(call.isTransmitting()) callState = CallState.TRANSMITTING;
			conversationEntity.getConversation().setCallType(callType);
			conversationEntity.getConversation().setCallInfo(callConverter.toDTO(call));
		}
		conversationEntity.getConversation().setCallState(callState);
	}
}
