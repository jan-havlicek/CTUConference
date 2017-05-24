package cz.ctu.ctuconference.conversation.domain;

import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.user.AppUser;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nick Nemame on 02.10.2016.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="conversation_type")
@Table(name="conversation")
abstract public class Conversation extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Basic
	private String name;

	@Column(name = "conversation_type", insertable = false, updatable = false)
	private String conversationType;

	@OneToMany(mappedBy="conversation")
	protected List<Message> messageList;

	abstract public List<AppUser> getParticipantList();

	public void addMessage(Message message) {
		messageList.add(message);
	}

	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public abstract String getContactName();

	public void setName(String name) {
		this.name = name;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public String getConversationType() {
		return conversationType;
	}

	public void setConversationType(String conversationType) {
		this.conversationType = conversationType;
	}
}
