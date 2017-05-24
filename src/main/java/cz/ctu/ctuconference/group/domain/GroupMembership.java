/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.group.domain;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.utils.dao.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author Nick nemame
 */
@Entity
@Table(name = "group_membership")
public class GroupMembership extends AbstractEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="user_id")
    protected AppUser user;

    @ManyToOne
    @JoinColumn(name="group_id")
    protected Group group;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="membership_state")
	private ContactAuthState state;

	@Basic(optional = false)
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	@Column(name="membership_role")
	private MembershipRole role;

    @Basic(optional = true)
    @Temporal(TemporalType.DATE)
    @Column(name="date_changed")
    private Date dateChanged;

	public boolean isAccepted() {
		return state == ContactAuthState.ACCEPTED;
	}

	public boolean isRequested() {
		return state == ContactAuthState.REQUESTED;
	}

    public GroupMembership() {
    }

    public GroupMembership(Long id) {
        this.id = id;
    }

	public GroupMembership(AppUser user, Group group, ContactAuthState state, MembershipRole role, Date dateChanged) {
		this.user = user;
		this.group = group;
		this.state = state;
		this.role = role;
		this.dateChanged = dateChanged;
	}

	public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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

	public MembershipRole getRole() {
		return role;
	}

	public void setRole(MembershipRole role) {
		this.role = role;
	}
}
