package cz.ctu.ctuconference.call.dto;

import cz.ctu.ctuconference.call.domain.CallType;

/**
 * Created by Nick nemame on 30.10.2016.
 */
public class CallInfoDTO {

	private long initiatorId;
	private String initiatorName;
	private long conversationId;
	private String callName;
	private CallType callType;

	public CallInfoDTO(long initiatorId, String initiatorName, long conversationId, String callName, CallType callType) {
		this.initiatorId = initiatorId;
		this.initiatorName = initiatorName;
		this.conversationId = conversationId;
		this.callName = callName;
		this.callType = callType;
	}

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

	public CallType getCallType() {
		return callType;
	}

	public void setCallType(CallType callType) {
		this.callType = callType;
	}
}
