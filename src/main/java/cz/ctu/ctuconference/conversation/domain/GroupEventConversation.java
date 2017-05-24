package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.group.domain.GroupEvent;
import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@DiscriminatorValue("E")
@Table(name="group_event_conversation")
public class GroupEventConversation extends Conversation {

	@OneToOne
	@JoinColumn(name="group_event_id")
	private GroupEvent groupEvent;

	@Override
	public String getContactName() {
		return groupEvent.getName();
	}

	@Override
	public List<AppUser> getParticipantList() {
		return groupEvent.getMemberList();
	}

	public GroupEvent getGroupEvent() {
		return groupEvent;
	}

	public void setGroupEvent(GroupEvent groupEvent) {
		this.groupEvent = groupEvent;
	}
}
