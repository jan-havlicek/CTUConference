/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.conversation.dto;

import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * This DTO is used for basic conversation information.
 * It is part of information send with whole contact list. It contains information
 * about contact list items and each contact item contains this conversation information.
 *
 * The name property is relevant only for multichat conversation.
 *
 * @author Nick nemame
 */
public class ConversationDTO implements Serializable {

    private long id;
    private String name;
	private List<ParticipantDTO> participantList;
	private CallState callState;
	private CallType callType;
	private CallInfoDTO callInfo;

	public ConversationDTO(long id, String name, List<ParticipantDTO> participantList) {
		this.id = id;
		this.name = name;
		this.participantList = participantList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParticipantDTO> getParticipantList() {
		return participantList;
	}

	public void setParticipantList(List<ParticipantDTO> participantList) {
		this.participantList = participantList;
	}

	public CallState getCallState() {
		return callState;
	}

	public void setCallState(CallState callState) {
		this.callState = callState;
	}

	public CallType getCallType() {
		return callType;
	}

	public void setCallType(CallType callType) {
		this.callType = callType;
	}

	public CallInfoDTO getCallInfo() {
		return callInfo;
	}

	public void setCallInfo(CallInfoDTO callInfo) {
		this.callInfo = callInfo;
	}

	@Override
    public String toString() {
        return "CONVERSATION_DTO - id:"+id;
    }

}
