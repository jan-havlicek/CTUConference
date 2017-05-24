package cz.ctu.ctuconference.group.dto;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.group.domain.MembershipRole;

/**
 * Created by Nick nemame on 29.12.2016.
 */
public class MembershipDTO {

	private long id;

	private long userId;

	private String email;

	private String firstName;

	private String lastName;

	private ContactAuthState state;

	private MembershipRole role;

	public MembershipDTO(long id, long userId, String email, String firstName, String lastName, ContactAuthState state, MembershipRole role) {
		this.id = id;
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.state = state;
		this.role = role;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public ContactAuthState getState() {
		return state;
	}

	public void setState(ContactAuthState state) {
		this.state = state;
	}

	public MembershipRole getRole() {
		return role;
	}

	public void setRole(MembershipRole role) {
		this.role = role;
	}
}
