/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.conversation.domain.GroupConversation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Nick nemame
 */
@Entity
@Table(name = "user_group")
public class Group extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Basic(optional = true)
    private String name;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="type")
	private GroupType type;

//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @JoinTable(
//            name = "user_for_group",
//            joinColumns = {@JoinColumn(name = "group_id")},
//            inverseJoinColumns = {@JoinColumn(name="user_id")})
	@OneToMany(mappedBy="group")
    private List<GroupMembership> membershipList;

	@OneToMany(mappedBy="group")
	private List<GroupEvent> eventList;

	@OneToMany(mappedBy="group")
	private List<GroupTopic> topicList;

	@OneToOne(mappedBy="group", cascade = CascadeType.PERSIST)
	private GroupConversation conversation;

	@Basic(optional = true)
	@Column(name="active")
	private boolean active;

	@Basic(optional = true)
	@Temporal(TemporalType.DATE)
	@Column(name="date_created")
	private Date dateCreated;


	public Group() {
    }

    public Group(String name, GroupType type) {
		this.name = name;
		this.type = type;
		this.active = true;

	}

    public Group(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public GroupType getType() {
		return type;
	}

	public void setType(GroupType type) {
		this.type = type;
	}

	public List<GroupMembership> getMembershipList() {
		return membershipList;
	}

	public void setMembershipList(List<GroupMembership> membershipList) {
		this.membershipList = membershipList;
	}

	public void addMembership(GroupMembership membership) {
		if(membershipList == null) {
			membershipList = new ArrayList<>();
		}
		membershipList.add(membership);
	}

	public List<GroupEvent> getEventList() {
		return eventList;
	}

	public void setEventList(List<GroupEvent> eventList) {
		this.eventList = eventList;
	}

	public List<GroupTopic> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<GroupTopic> topicList) {
		this.topicList = topicList;
	}

	public GroupConversation getConversation() {
		return conversation;
	}

	public void setConversation(GroupConversation conversation) {
		this.conversation = conversation;
	}

	public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
