/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.authentication.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Nick nemame
 */
public class LoginDTO implements Serializable {

    private String login;

	private String password;

	public LoginDTO() {}

	public LoginDTO(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
