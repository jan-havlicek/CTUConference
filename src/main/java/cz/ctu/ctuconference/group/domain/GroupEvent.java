/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.conversation.domain.GroupEventConversation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Nick nemame
 */
@Entity
@Table(name = "group_event")
public class GroupEvent extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIME)
    @Column(name="date_from")
    private Date from;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIME)
    @Column(name="date_to")
    private Date to;

    @Basic(optional = false)
    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

	@NotNull
	@OneToOne(mappedBy = "groupEvent")
	protected GroupEventConversation conversation;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "user_for_group_event",
			joinColumns = {@JoinColumn(name = "group_event_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<AppUser> memberList;


    public GroupEvent() {
    }

    public GroupEvent(Long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

	public GroupEventConversation getConversation() {
		return conversation;
	}

	public void setConversation(GroupEventConversation conversation) {
		this.conversation = conversation;
	}

	public List<AppUser> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<AppUser> memberList) {
		this.memberList = memberList;
	}
}
