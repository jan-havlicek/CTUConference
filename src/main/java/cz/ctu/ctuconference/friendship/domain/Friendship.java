/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.friendship.domain;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.conversation.domain.PrivateConversation;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Nick nemame
 */
@Entity
@Table(name = "friendship")
public class Friendship extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="user_id")
    protected AppUser user;

    @ManyToOne
    @JoinColumn(name="friend_id")
    protected AppUser friend;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="conversation_id")
    protected PrivateConversation conversation;

    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name="friendship_state")
    private ContactAuthState state;

    @Basic(optional = true)
    @Temporal(TemporalType.DATE)
    @Column(name="date_changed")
    private Date dateChanged;


    public Friendship() {
    }

    public Friendship(Long id) {
        this.id = id;
    }

	public Friendship(AppUser user, AppUser friend, PrivateConversation conversation, ContactAuthState state, Date dateChanged) {
		this.user = user;
		this.friend = friend;
		this.conversation = conversation;
		this.state = state;
		this.dateChanged = dateChanged;
	}

	public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public AppUser getFriend() {
        return friend;
    }

    public void setFriend(AppUser friend) {
        this.friend = friend;
    }

    public PrivateConversation getConversation() {
        return conversation;
    }

    public void setConversation(PrivateConversation conversation) {
        this.conversation = conversation;
    }

    public ContactAuthState getState() {
        return state;
    }

    public void setState(ContactAuthState state) {
        this.state = state;
    }

    public Date getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }

}
