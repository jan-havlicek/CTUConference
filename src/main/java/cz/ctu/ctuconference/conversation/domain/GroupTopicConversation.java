package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.group.domain.GroupTopic;
import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@DiscriminatorValue("T")
@Table(name="group_topic_conversation")
public class GroupTopicConversation extends Conversation {

	@OneToOne
	@JoinColumn(name="group_topic_id")
	private GroupTopic groupTopic;

	@Override
	public String getContactName() {
		return groupTopic.getName();
	}

	@Override
	public List<AppUser> getParticipantList() {
		return groupTopic.getMemberList();
	}

	public GroupTopic getGroupTopic() {
		return groupTopic;
	}

	public void setGroupTopic(GroupTopic groupTopic) {
		this.groupTopic = groupTopic;
	}
}
