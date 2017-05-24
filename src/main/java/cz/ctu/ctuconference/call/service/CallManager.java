/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.*;

import java.io.IOException;
import java.util.List;

import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 *
 * @author Nick Nemame
 */
@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class CallManager {

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<Long, Call> calls = new ConcurrentHashMap<>();

	/**
	 * Create selected type of call and put it to tle list of calls.
	 * @param callType
	 * @param initiatorId
	 * @param initiatorName
	 * @param conversationId
	 * @param callName
	 * @param conversationMembers
	 * @return
	 * @throws IOException
	 */
    public Call createCall(CallType callType, long initiatorId, String initiatorName, long conversationId, String callName, List<Long> conversationMembers) throws IOException {
		Call call = null;
		switch(callType) {
			case VOICE_CALL:
				call = new VoiceCall(initiatorId, initiatorName, conversationId, callName, conversationMembers, kurento.createMediaPipeline());
				break;
			case VIDEO_CALL:
				call = new VideoCall(initiatorId, initiatorName, conversationId, callName, conversationMembers, kurento.createMediaPipeline());
				break;
			case WEBINAR:
				call = new WebinarCall(initiatorId, initiatorName, conversationId, callName, conversationMembers, kurento.createMediaPipeline());
				break;
		}
		call.getPipeline().setLatencyStats(true);
		calls.put(conversationId, call);
		return call;
	}

	/**
	 * Returns the call, that initiator invoked
	 * @param initiatorId
	 * @return
	 */
	public Call getCallByInitiator(long initiatorId) {
		for(Long conversationId : calls.keySet()) {
			Call call = calls.get(conversationId);
			if(initiatorId == call.getInitiatorId()) {
				return call;
			}
		}
		return null;
	}

	/**
	 * Get call for the selected conversation. There could be only one call for one conversation at a time.
	 * @param conversationId
	 * @return
	 */
	public Call getCall(long conversationId) {
		Call call = calls.get(conversationId);
		//log.debug("Conversation {} found!", conversation.getId()+"");
		return call;
	}

    /**
     * Removes the call.
     *
     * @param call
     */
    public void removeCall(Call call) {
        this.calls.remove(call.getConversationId());
        call.close();
        //log.info("Room {} removed and closed", room.getValue());
    }

	/**
	 * It returns true if the user is in the call
	 * (that is tolling or already transmitting)
	 */
	public boolean isUserBusy(long userId) {
		final boolean[] isBusy = {false};
		calls.forEach((conversationId, call) -> {
			if(call.hasParticipant(userId)
			|| call.hasPendingParticipant(userId)) {
				isBusy[0] = true;
			}
		});
		return isBusy[0];
	}
}
