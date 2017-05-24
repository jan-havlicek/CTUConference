/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.conversation.domain.GroupTopicConversation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Nick nemame
 */
@Entity
@Table(name = "group_topic")
public class GroupTopic extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Column(name="name")
    private String name;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "group_id")
	private Group group;

    @NotNull
    @OneToOne(mappedBy = "groupTopic")
    protected GroupTopicConversation conversation;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "user_for_group_topic",
			joinColumns = {@JoinColumn(name = "group_topic_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")})
	private List<AppUser> memberList;


    public GroupTopic() {
    }

    public GroupTopic(Long id) {
        this.id = id;
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

	public GroupTopicConversation getConversation() {
		return conversation;
	}

	public void setConversation(GroupTopicConversation conversation) {
		this.conversation = conversation;
	}

	public List<AppUser> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<AppUser> memberList) {
		this.memberList = memberList;
	}
}
