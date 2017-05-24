package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@DiscriminatorValue("G")
@Table(name="group_conversation")
public class GroupConversation extends Conversation {

	@OneToOne
	@JoinColumn(name="group_id")
	private Group group;

	@Override
	public List<AppUser> getParticipantList() {
		return group.getMembershipList().stream()
				.filter(membership -> membership.isAccepted())
				.map(membership -> membership.getUser())
				.collect(Collectors.toList());
	}

	@Override
	public String getContactName() {
		return group.getName();
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
}
