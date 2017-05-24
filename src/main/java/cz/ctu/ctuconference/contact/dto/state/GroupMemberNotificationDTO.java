package cz.ctu.ctuconference.contact.dto.state;

import cz.ctu.ctuconference.contact.domain.ContactAction;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.conversation.dto.ParticipantDTO;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class GroupMemberNotificationDTO extends GroupNotificationDTO {
	private Long memberId;
	private ContactState state;
	private ParticipantDTO member;
	private long conversationId;

	public GroupMemberNotificationDTO(Long memberId, ContactAction action, ContactState state) {
		super(null, action);
		this.memberId = memberId;
		this.state = state;
		this.member = null;
		this.conversationId = 0;
	}

	public GroupMemberNotificationDTO(ParticipantDTO member, long conversationId) {
		super(null, ContactAction.ADDED);
		this.memberId = null;
		this.state = ContactState.ONLINE;
		this.member = member;
		this.conversationId = conversationId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public ContactState getState() {
		return state;
	}

	public void setState(ContactState state) {
		this.state = state;
	}

	public ParticipantDTO getMember() {
		return member;
	}

	public void setMember(ParticipantDTO member) {
		this.member = member;
	}

	public long getConversationId() {
		return conversationId;
	}

	public void setConversationId(long conversationId) {
		this.conversationId = conversationId;
	}
}
