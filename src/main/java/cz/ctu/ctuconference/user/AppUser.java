/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.user;

import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.conversation.domain.MultichatConversation;
import cz.ctu.ctuconference.friendship.domain.Friendship;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Nick nemame
 */
//@Configurable(preConstruction=true)
@Entity
@Table(name = "app_user")
public class AppUser extends AbstractEntity {
    protected static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    protected String email;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name")
    protected String firstName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "last_name")
    protected String lastName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 59, max = 60)
    @Column(name = "password")
    protected String password;

    @Lob
    @Column(name = "avatar")
    protected byte[] avatar;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "role_for_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")})
    protected List<AppRole> roleList;


    @OneToMany(mappedBy="user")
    protected List<Friendship> friendList;
    /*
    @OneToMany(mappedBy="friend")
    protected List<Friendship> friendOfList;
    */
    @OneToMany(mappedBy="user")
    protected List<GroupMembership> groupMembershipList;

    @ManyToMany(mappedBy="participantList")
    protected List<MultichatConversation> multichatList;

    public AppUser() {
    }

    public AppUser(Long id) {
        this.id = id;
    }

    public AppUser(Long id, String email, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFullName() {
		return firstName + " " + lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public List<AppRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<AppRole> roleList) {
		this.roleList = roleList;
	}

	public List<Friendship> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<Friendship> friendList) {
		this.friendList = friendList;
	}

	public List<GroupMembership> getGroupMembershipList() {
		return groupMembershipList;
	}

	public void setGroupMembershipList(List<GroupMembership> groupMembershipList) {
		this.groupMembershipList = groupMembershipList;
	}

	public void addGroupMembership(GroupMembership groupMembership) {
		if(this.groupMembershipList == null) this.groupMembershipList = new ArrayList<>();
		this.groupMembershipList.add(groupMembership);
	}

	public List<MultichatConversation> getMultichatList() {
		return multichatList;
	}

	public void setMultichatList(List<MultichatConversation> multichatList) {
		this.multichatList = multichatList;
	}
}
