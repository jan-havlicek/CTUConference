package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@DiscriminatorValue("M")
@Table(name="multichat_conversation")
public class MultichatConversation extends Conversation {

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "user_for_multichat_conversation",
			joinColumns = {@JoinColumn(name = "conversation_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<AppUser> participantList;

	public List<AppUser> getParticipantList() {
		return participantList;
	}

	@Override
	public String getContactName() {
		return getDefaultName();
	}

	public void setParticipantList(List<AppUser> participantList) {
		this.participantList = participantList;
	}

	public String getDefaultName() {
		String name = "";
		for(AppUser participant : getParticipantList()) {
			if(!name.equals("")) name += ", ";
			name += participant.getFirstName();
		}
		return name;
	}

	public String getDefaultTitle() {
		String title = "";
		for(AppUser participant : getParticipantList()) {
			if(!title.equals("")) title += ", ";
			title += participant.getFullName();
		}
		return title;
	}
}
