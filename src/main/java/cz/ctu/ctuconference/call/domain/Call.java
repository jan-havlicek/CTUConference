/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.call.domain;

import cz.ctu.ctuconference.attachment.domain.Attachment;
import cz.ctu.ctuconference.call.dto.CallParticipantLeftDTO;
import cz.ctu.ctuconference.call.dto.ExistingParticipantsDTO;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;
import cz.ctu.ctuconference.call.dto.ParticipantInfoDTO;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.user.UserSession;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.kurento.client.MediaPipeline;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.kurento.client.Continuation;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Call is created when some user initiates new call.
 * It is removed when user cancels the call before its start or when the last
 * participants of call will terminate it.
 *
 * When there is call created but no participant is present,
 * it is in "toll" phase. When there are participants, it is transmitting.
 * @author Nick Nemame
 */
public abstract class Call implements Closeable {
	/**
	 * Id of user who initiated the call - lector or calling_member
	 */
	private long initiatorId;

	/**
	 * Name of user who initiated the call
	 */
	private String initiatorName;

	/**
	 * Conversation, which the call is performed in.
	 */
	private long conversationId;

	/**
	 * Name of the call - in default it should be name of the group or friend represented by conversation id.
	 */
	private String callName;

	/**
	 * All members of the conversation
	 */
	protected final List<Long> members;

	/**
	 * Participants currently in the call - everyone connected regardless of whether they are performing or just listeners
	 * The type of participant is described in users UserCall (it is contained in UserSession)
	 */
	protected final ConcurrentMap<Long, UserSession> participants = new ConcurrentHashMap<>();

	/**
	 * Participants that are tolling - they are busy for other callers but still waiting to accept or refuse the call.
	 */
	protected final ConcurrentMap<Long, UserSession> pendingParticipants = new ConcurrentHashMap<>();

	/**
	 * Media pipeline for the call - the user call endpoints for users in this call are connected to this endpoint
	 */
	private MediaPipeline pipeline;
	private Attachment handouts;
	private long handoutsPage = 1;

	public Call(long initiatorId, String initiatorName, long conversationId, String callName, List<Long> members, MediaPipeline pipeline) throws IOException {
		this.initiatorId = initiatorId;
		this.initiatorName = initiatorName;
		this.conversationId = conversationId;
		this.callName = callName;
		this.members = members;
		this.pipeline = pipeline;
	}


	/** Call basic info - getters and setters **/

	public long getInitiatorId() {
		return initiatorId;
	}

	public void setInitiatorId(long initiatorId) {
		this.initiatorId = initiatorId;
	}

	public String getInitiatorName() {
		return initiatorName;
	}

	public void setInitiatorName(String initiatorName) {
		this.initiatorName = initiatorName;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}

	public String getCallName() {
		return callName;
	}

	public void setCallName(String callName) {
		this.callName = callName;
	}

	public abstract CallType getCallType();


	/** Participant handling **/

	private void addPendingParticipant(UserSession userSession) {
		pendingParticipants.put(userSession.getUserId(), userSession);
	}

	public void removePendingParticipant(long userId) {
		pendingParticipants.remove(userId);
	}

	private void addParticipant(UserSession userSession) {
		participants.put(userSession.getUserId(), userSession);
	}

	/**
	 * Get all participants, that are joined the call. They are listeners or are broadcasting audiovisual signal
	 * @return
	 */
	public Collection<UserSession> getParticipants() {
		return participants.values();
	}

	/**
	 * Get participant, that joined the call
	 * @param participantId
	 * @return
	 */
	public UserSession getParticipant(long participantId) {
		return participants.get(participantId);
	}

	/**
	 * Get all users, that are tolling - the message about the call is sent to them
	 * @return
	 */
	public Collection<UserSession> getPendingParticipants() {
		return pendingParticipants.values();
	}

	/**
	 * Get all participants, that are transmittin audio or video signal. It excludes listeners, that does not broadcast
	 * audiovisual signal
	 * @return
	 */
	public List<UserSession> getTransmittingParticipants() {
		return participants.values()
				.stream()
				.filter(participantSession -> participantSession.isTransmitting())
				.collect(Collectors.toList());
	}

	public List<UserSession> getListeningParticipants() {
		return participants.values()
				.stream()
				.filter(participantSession -> !participantSession.isTransmitting())
				.collect(Collectors.toList());
	}
	/**
	 * Check if there is participant
	 * @param userId
	 * @return
	 */
	public boolean hasParticipant(long userId) {
		return participants.containsKey(userId);
	}

	/**
	 * Check if there is participant, that does not joined yet
	 * @param userId
	 * @return
	 */
	public boolean hasPendingParticipant(long userId) {
		return pendingParticipants.containsKey(userId);
	}

	public boolean hasPendingParticipants() {
		return pendingParticipants.size() > 0;
	}


	/** Call actions and events handling **/

	/**
	 * When user is invited to the call, they will be added as pending participant.
	 * @param userSession
	 */
	public void onInviteToCall(UserSession userSession) {
		addPendingParticipant(userSession);
	}

	/**
	 * When the user is joined to the call, it is necessary to check, if they have been in pending participants.
	 * It is the case, when in the time of call initiation the user was available to call and the call start tolling.
	 * In the case user arrived after the call initiation, they are not in the pending participants.
	 * @param userSession
	 */
	public void join(UserSession userSession, ParticipantInfo participantInfo) {
		//move participant from pending to calling
		long userId = userSession.getUserId();
		if(hasPendingParticipant(userId)) {
			removePendingParticipant(userSession.getUserId());
		}
		addParticipant(userSession);

		//create UserCall and add to call pipeline
		userSession.initCall(conversationId, participantInfo, pipeline);
	}

	/**
	 * Leave the call
	 * @param userSession
	 * @throws IOException
	 */
	public void leave(UserSession userSession) throws IOException {
		participants.remove(userSession.getUserId());
	}

	/**
	 * Get info about the joined participants
	 * @param userSession
	 * @return
	 * @throws IOException
	 */
	public List<ParticipantInfo> getParticipantInfoList(UserSession userSession) throws IOException {
		return participants.values().stream()
				.filter(participantSession -> !participantSession.equals(userSession))
				.map(participantSession -> participantSession.getUserCall().getParticipantInfo())
				.collect(Collectors.toList());
	}

	/**
	 * Get info about the transmitting joined participants
	 * @param userSession
	 * @return
	 * @throws IOException
	 */
	public List<ParticipantInfo> getTransmittingParticipantInfoList(UserSession userSession) throws IOException {
		return participants.values().stream()
				.filter(participantSession -> !participantSession.equals(userSession) && participantSession.isTransmitting())
				.map(participantSession -> participantSession.getUserCall().getParticipantInfo())
				.collect(Collectors.toList());
	}


	/** Call state handling **/

	/**
	 * Info whether it is in requesting state (tolling started, but nobody joined yet.
	 * @return
	 */
	public boolean isRequesting() {
		return participants.size() == 0;
	}

	/**
	 * Info whether it is in voice/video transmission state.
	 * @return
	 */
	public boolean isTransmitting() {
		return participants.size() > 0;
	}


	/** Handouts handling **/

	public void setHandouts(Attachment handouts) {
		this.handouts = handouts;
		this.handoutsPage = 1;
	}

	public Attachment getHandouts() {
		return handouts;
	}

	public void setHandoutsPage(long page) {
		this.handoutsPage = page;
	}

	public long getHandoutsPage() {
		return handoutsPage;
	}


	@PreDestroy
	private void shutdown() {
		this.close();
	}

	@Override
	public void close() {
		for (final UserSession user : participants.values()) {
			try {
				user.endCall();
			} catch (IOException e) {
				//log.debug("ROOM {}: Could not invoke close on participant {}", this.name, user.getValue(), e);
			}
		}
		participants.clear();
		pendingParticipants.clear();
		pipeline.release(new Continuation<Void>() {
			@Override
			public void onSuccess(Void result) throws Exception {
				//log.trace("ROOM {}: Released Pipeline", Room.this.name);
			}
			@Override
			public void onError(Throwable cause) throws Exception {
				//log.warn("PARTICIPANT {}: Could not release Pipeline", Room.this.name);
			}
		});
		//log.debug("Room {} closed", this.name);
	}

	public MediaPipeline getPipeline() {
		return pipeline;
	}

	public void setPipeline(MediaPipeline pipeline) {
		this.pipeline = pipeline;
	}

	public List<Long> getMembers() {
		return members;
	}

}
