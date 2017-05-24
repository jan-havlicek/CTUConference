/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.registration;

import java.io.Serializable;
import java.util.List;

/**
 * Trida predavajici JSF sablone info o uzivateli
 * @author Nick nemame
 */
public class RegistrationDTO implements Serializable {

	private String email;

	private String firstName;

	private String lastName;

	private String password;

	public RegistrationDTO() {}

	public RegistrationDTO(String email, String firstName, String lastName, String password) {
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
}
