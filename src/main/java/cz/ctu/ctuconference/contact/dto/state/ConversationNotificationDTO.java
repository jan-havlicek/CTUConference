package cz.ctu.ctuconference.contact.dto.state;

import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class ConversationNotificationDTO {
	protected long conversationId;
	protected CallState callState;
	protected CallType callType;
	protected CallInfoDTO callInfo;

	public ConversationNotificationDTO(long conversationId, CallState callState, CallType callType, CallInfoDTO callInfo) {
		this.conversationId = conversationId;
		this.callState = callState;
		this.callType = callType;
		this.callInfo = callInfo;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
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
}
