package cz.ctu.ctuconference.call.service.comm;

import cz.ctu.ctuconference.attachment.dto.HandoutsAddedDTO;
import cz.ctu.ctuconference.call.domain.*;
import cz.ctu.ctuconference.call.dto.*;
import cz.ctu.ctuconference.call.service.CallConverter;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.call.service.ParticipantInfoConverter;
import cz.ctu.ctuconference.call.service.ParticipantInfoFactory;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.contact.service.comm.ContactStateNotifierService;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.kurento.client.IceCandidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Nick nemame on 04.01.2017.
 */
@Service
public class CallCommServiceImpl implements CallCommService {

	@Autowired
	private AppMessageSender messageSender;

	@Autowired
	private CallConverter callConverter;

	@Autowired
	private ContactStateNotifierService notifierService;

	@Autowired
	private CallManager callManager;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private UserRegistry userRegistry;

	@Autowired
	private ParticipantInfoFactory participantInfoFactory;

	@Autowired
	private ParticipantInfoConverter participantInfoConverter;

	@Override
	public void handleUserSessionClose(UserSession userSession) throws IOException {
		notifierService.notifyUserStateChanged(userSession.getUserId(), ContactState.OFFLINE);
		if(userSession.isInCall()) {
			leaveCall(userSession);
			userSession.close();
		}
	}

	@Override
	public void leaveCall(UserSession userSession) throws IOException {
		long conversationId = userSession.getUserCall().getConversationId();
		Call call = callManager.getCall(conversationId);
		leave(call, userSession, true);
		if(call.getCallType() == CallType.WEBINAR) {
			onWebinarLeft(userSession, (WebinarCall) call);
		} else {
			onGroupCallLeft(userSession, call);
		}
	}

	/**
	 * If the user was lector, that initiated the call, it is necessary to terminate the call and
	 * notify all participant and still pending participant about the call end.
	 * If the user was detached cameraman, it is necessary to start stream from the initiating lector.
	 * @param call
	 */
	private void onWebinarLeft(UserSession userSession, WebinarCall call) throws IOException {
		if(userSession.getUserId() == call.getInitiatorId()) {
			for(UserSession participantSession : call.getParticipants()) {
				leave(call, participantSession, false);
				messageSender.send(participantSession, "call.terminated", new CallTerminatedDTO(call.getConversationId()));
			}
			if(call.hasPendingParticipants()) {
				messageSender.sendToPendingParticipants(call.getConversationId(), -1, "call.terminated", new CallTerminatedDTO(call.getConversationId()), null);
			}
			notifierService.notifyGroupCallState(call.getConversationId(), CallState.NONE, call.getCallType(), null);
			callManager.removeCall(call);
		} else if(call.isDetachedCameraman(userSession)) {
			//@todo userSession.getCallParticipantInfo()...
		} else {

		}
	}

	/**
	 * After the user left the call, it is necessary to check, if there is enough participants to continue the call.
	 * If there is just one participant left, the call will be terminated and the last participant will be notified
	 * about the call end. It is necessary to notify all users, that are still pending.
	 * @param call
	 * @throws IOException
	 */
	private void onGroupCallLeft(UserSession userSession, Call call) throws IOException {
		if(call.getParticipants().size() == 1) {
			UserSession lastParticipantSession = call.getParticipants().iterator().next();
			leave(call, lastParticipantSession, false);
			messageSender.send(lastParticipantSession, "call.terminated", new CallTerminatedDTO(call.getConversationId()));
			if(call.hasPendingParticipants()) {
				messageSender.sendToPendingParticipants(call.getConversationId(), -1, "call.terminated", new CallTerminatedDTO(call.getConversationId()), null);
			}
			notifierService.notifyGroupCallState(call.getConversationId(), CallState.NONE, call.getCallType(), null);
			callManager.removeCall(call);
		}
	}

	/**
	 * Create new call and invite all conversation members to this call. If the call is webinar, it will start immediately
	 * and the initiator is joined
	 * @param initiatorSession
	 * @param conversationId
	 * @param callType
	 * @throws IOException
	 */
	@Override
	public void invokeCall(UserSession initiatorSession, long conversationId, CallType callType) throws IOException {
		List<Long> members = conversationService.getParticipantIdList(conversationId);
		String callName = conversationService.getById(conversationId).getContactName();
		Call call = callManager.createCall(callType, initiatorSession.getUserId(), initiatorSession.getUserName(), conversationId, callName, members);
		inviteMembersToCall(call);
		if(callType == CallType.WEBINAR) {
			// if it is webinar, it will join initiator and then send the initiator info about call started
			messageSender.send(initiatorSession, "call.started", new CallStartedDTO(conversationId));
			ParticipantInfo participantInfo = participantInfoFactory.create(initiatorSession.getUserId(), call.getCallType(), true);
			join(call, initiatorSession, participantInfo);
			notifierService.notifyGroupCallState(call.getConversationId(), CallState.TRANSMITTING, call.getCallType(), callConverter.toDTO(call));
		} else {
			notifierService.notifyGroupCallState(call.getConversationId(), CallState.REQUESTING, call.getCallType(), callConverter.toDTO(call));
		}
	}

	/**
	 *
	 * @param call
	 * @throws IOException
	 */
	private void inviteMembersToCall(Call call) throws IOException {
		CallInfoDTO info = callConverter.toDTO(call);
		messageSender.sendToAvailableMembers(call.getConversationId(), call.getInitiatorId(), "call.request", info,
				participantSession -> call.onInviteToCall(participantSession));
	}

	/**
	 * Join user to the call
	 *
	 * For AUDIO or VIDEO call: the call will START when the first participant will join. It is necessary to join
	 * the initiator too.
	 * For WEBINAR: There is nothing special to do - call started already when the initiator invited the participants.
	 * @param conversationId
	 * @param userSession
	 * @throws IOException
	 */
	@Override
	public void joinCall(long conversationId, UserSession userSession) throws IOException {
		Call call = callManager.getCall(conversationId);
		// Join the user - this user is not an initiator (in webinar initiator is joined automatically during invitation
		// and in other calls initiator is joined automatically when first participant is joined
		ParticipantInfo participantInfo = participantInfoFactory.create(userSession.getUserId(), call.getCallType(), false);
		join(call, userSession, participantInfo);

		//if it is not webinar, it is necessary to join also the initiator, that is waiting for another participant
		if (call.getCallType() != CallType.WEBINAR) {
			//If it is first user joined, it is necessary to join the caller too
			UserSession initiatorSession = userRegistry.getByUserId(call.getInitiatorId());
			if(initiatorSession != null && !initiatorSession.isInCall()) {
				messageSender.send(initiatorSession, "call.started", new CallStartedDTO(conversationId));
				ParticipantInfo initiatorInfo = participantInfoFactory.create(call.getInitiatorId(), call.getCallType(), true);
				join(call, initiatorSession, initiatorInfo);
				notifierService.notifyGroupCallState(call.getConversationId(), CallState.TRANSMITTING, call.getCallType(), callConverter.toDTO(call));
			}
		}
	}

	/**
	 * Join the user to the call. User will be removed from pending participants.
	 *
	 * Send info about new participant to existing participants
	 * and send list of existing participants to new participant
	 *
	 * Notify user joined the call info
	 *
	 * @param userSession
	 * @throws IOException
	 */
	private void join(Call call, UserSession userSession, ParticipantInfo participantInfo) throws IOException {
		call.join(userSession, participantInfo);
		sendNewParticipantInfo(call, userSession, participantInfo);
		sendExistingParticipants(call, userSession, participantInfo);
		if(call.getHandouts() != null) {
			//send notification with added handouts
			HandoutsAddedDTO message = new HandoutsAddedDTO(1, call.getConversationId());
			messageSender.send(userSession, "handouts.added", message);
		}
		notifierService.notifyUserInCall(userSession.getUserId(), call.getConversationId());
	}

	/**
	 * The info about new participant will be sent to all participant if the user is transmitting.
	 * or will be sent only to transmitting user if the user is just listener (listeners is not necessary to connect).
	 *
	 * @param call
	 * @param userSession
	 * @param participantInfo
	 */
	private void sendNewParticipantInfo(Call call, UserSession userSession, ParticipantInfo participantInfo) throws IOException {
		//send new participant info to all already joined participants
		ParticipantInfoDTO participantInfoDTO = participantInfoConverter.toDTO(participantInfo);
		if(participantInfo.isTransmitting()) {
			messageSender.sendToCallingParticipants(call.getConversationId(), userSession.getUserId(), "call.newParticipantArrived", participantInfoDTO, null);
		} else {
			messageSender.sendToTransmittingParticipants(call.getConversationId(), userSession.getUserId(), "call.newParticipantArrived", participantInfoDTO, null);
		}
	}

	/**
	 * User is also informed about existing participants and will be informed about all participants if the user
	 * is transmitting, or about all transmitting participants if the user is just listener.
	 * If the user is not transmitting, the lector will not receive their media. It is only used
	 * to list all participants
	 *
	 * @param call
	 * @param userSession
	 * @param participantInfo
	 * @throws IOException
	 */
	private void sendExistingParticipants(Call call, UserSession userSession, ParticipantInfo participantInfo) throws IOException {
		//send already joined participant list to new participant along with participant's own info.
		ParticipantInfoDTO participantInfoDTO = participantInfoConverter.toDTO(participantInfo);
		ExistingParticipantsDTO participantsDTO = null;
		if(participantInfo.isTransmitting()) {
			participantsDTO = new ExistingParticipantsDTO(participantInfoConverter.toDTOList(call.getParticipantInfoList(userSession)), participantInfoDTO);
		} else {
			participantsDTO = new ExistingParticipantsDTO(participantInfoConverter.toDTOList(call.getTransmittingParticipantInfoList(userSession)), participantInfoDTO);
		}
		messageSender.send(userSession, "call.existingParticipants", participantsDTO);
	}

	@Override
	public void cancelCall(UserSession userSession) throws IOException {
		Call call = callManager.getCallByInitiator(userSession.getUserId());
		messageSender.sendToCallParticipants(call.getConversationId(), -1, "call.terminated", new CallTerminatedDTO(call.getConversationId()));
		callManager.removeCall(call);
	}

	@Override
	public void rejectCall(long conversationId, UserSession userSession) throws IOException {
		Call call = callManager.getCall(conversationId);
		call.removePendingParticipant(userSession.getUserId());
		if(call.getCallType() != CallType.WEBINAR
				&& call.getPendingParticipants().isEmpty()
				&& call.getParticipants().isEmpty()) {
			//Ukonceni hovoru
			messageSender.send(call.getInitiatorId(), "call.terminated", new CallTerminatedDTO(call.getConversationId()));
			notifierService.notifyGroupCallState(call.getConversationId(), CallState.NONE, call.getCallType(), null);
			callManager.removeCall(call);
		}
	}

	/**
	 * Leave the call
	 * @param userSession
	 * @throws IOException
	 */
	private void leave(Call call, UserSession userSession, boolean sendParticipantLeft) throws IOException {
		call.leave(userSession);
		userSession.endCall();
		if(sendParticipantLeft) {
			sendParticipantLeft(call, userSession);
		}
		notifierService.notifyUserStopCalling(userSession.getUserId());
	}

	private void sendParticipantLeft(Call call, UserSession userSession) throws IOException {
		long userId = userSession.getUserId();
		long conversationId = call.getConversationId();
		CallParticipantLeftDTO participantLeftDTO = new CallParticipantLeftDTO(userId);
		messageSender.sendToCallingParticipants(conversationId, userId, "call.participantLeft", participantLeftDTO,
				participantSession -> participantSession.getUserCall().cancelVideoFrom(userId));
	}


	/**
	 * Receive media from sender. SDP offer is processed and SDP answer is sent back to the user. Then
	 * @param userSession
	 * @param senderId
	 * @param sdpOffer
	 * @throws IOException
	 */
	@Override
	public void receiveVideoFrom(UserSession userSession, long senderId, String sdpOffer) throws IOException {
		UserSession sender = userRegistry.getByUserId(senderId);
		UserCall userCall = userSession.getUserCall();
		String ipSdpAnswer = userCall.getSdpAnswer(sender, sdpOffer);
		messageSender.send(userSession, "call.receiveVideoAnswer", new SDPAnswerDTO(sender.getUserId(), ipSdpAnswer));
		userCall.gatherCandidates(sender);
		//userSession.receiveVideoFrom(sender, sdpOffer);
	}

	@Override
	public void handleIceCandidate(UserSession userSession, long participantId, IceCandidate candidate) {
		userSession.getUserCall().addCandidate(candidate, participantId);
	}

	/**
	 * Change participant info - role and transmission info
	 *
	 *  - It is necessary to notify the user whose info was changed about this change
	 *  - If the user is starting transmission, it is necessary to send to all listening participants
	 * "New participant arrived" message and all transmitting participants "participant changed" message.
	 *  - If the user is stopping transmission, it is necessary to send to all just listening participants
	 *  "Participant left" and send to all other transmitting participants "Participant changed".
	 *
	 * @param lectorSession
	 * @param participantInfo
	 */
	@Override
	public void modifyParticipant(UserSession lectorSession, ParticipantInfo participantInfo) throws IOException {
		//modify participant info. Initiator and participant type should not be modified.
		Call call = callManager.getCall(lectorSession.getUserCall().getConversationId());
		UserSession participantSession = call.getParticipant(participantInfo.getId());
		ParticipantInfo participantPreviousInfo = participantSession.getUserCall().getParticipantInfo();
		participantInfo.setInitiator(participantPreviousInfo.isInitiator());
		participantInfo.setParticipantType(participantPreviousInfo.getParticipantType());
		participantSession.getUserCall().setParticipantInfo(participantInfo);

		// send to user whose info was changed
		messageSender.send(participantInfo.getId(), "call.participantModified", participantInfoConverter.toDTO(participantInfo));

		//handle the situation when participant starts, stops transmission of media or just changes its role in the call.
		if(participantPreviousInfo.isTransmitting() && !participantInfo.isTransmitting()) {
			stopTransmitting(participantSession, call, participantInfo);
		} else if(!participantPreviousInfo.isTransmitting() && participantInfo.isTransmitting()) {
			startTransmitting(participantSession, call, participantInfo);
		} else {
			sendParticipantRoleModified(participantSession, call, participantInfo);
		}
	}

	/**
	 *
	 * @param userSession
	 * @param call
	 * @throws IOException
	 */
	private void stopTransmitting(UserSession userSession, Call call, ParticipantInfo participantInfo) throws IOException {
		long userId = userSession.getUserId();
		long conversationId = call.getConversationId();
		CallParticipantLeftDTO participantLeftDTO = new CallParticipantLeftDTO(userId);
		messageSender.sendToListeningParticipants(conversationId, userId, "call.participantLeft", participantLeftDTO,
				participantSession -> participantSession.getUserCall().cancelVideoFrom(userId));
		messageSender.sendToTransmittingParticipants(conversationId, userId, "call.participantModified", participantInfoConverter.toDTO(participantInfo),
				participantSession -> participantSession.getUserCall().cancelVideoFrom(userId));
		userSession.getUserCall().stopTransmitting();
	}

	/**
	 * Existing participant starts transmitting - it is necessary to inform listeners (who did not know about this user), that are new participants
	 * of this call, so they will send "receive video answer" message to connect to this user.
	 * Then it is necessary to send "participant modified" to transmitting users. If they find out, that this user is newly
	 * transmitting, they will send "receive video answer" too.
	 * So in the contrast of stop transmitting, there is no action now necessary - the connection of endpoints will be performed after
	 * users send the "receive video answer" message.
	 * @param userSession
	 * @param call
	 * @param participantInfo
	 */
	private void startTransmitting(UserSession userSession, Call call, ParticipantInfo participantInfo) throws IOException {
		long userId = userSession.getUserId();
		long conversationId = call.getConversationId();
		userSession.getUserCall().startTransmitting();
		messageSender.sendToListeningParticipants(conversationId, userId, "call.newParticipantArrived",participantInfoConverter.toDTO(participantInfo), null);
		messageSender.sendToTransmittingParticipants(conversationId, userId, "call.participantModified", participantInfoConverter.toDTO(participantInfo), null);
	}

	/**
	 * This will be performed when the user is not either starting nor stopping the transmittion of media. It is performed
	 * only when role is changed (or type of transmitted media changed, but this functionality is not implemented yet)
	 * @param userSession
	 * @param call
	 * @param participantInfo
	 * @throws IOException
	 */
	private void sendParticipantRoleModified(UserSession userSession, Call call, ParticipantInfo participantInfo) throws IOException {
		long userId = userSession.getUserId();
		long conversationId = call.getConversationId();
		messageSender.sendToCallingParticipants(conversationId, userId, "call.participantModified", participantInfoConverter.toDTO(participantInfo), null);
	}

}
