package cz.ctu.ctuconference.authentication.dto;

import cz.ctu.ctuconference.user.UserDTO;

/**
 * Created by Nick nemame on 24.12.2016.
 */
public class AuthenticationDTO {
	private boolean state;
	private UserDTO user;

	public AuthenticationDTO(boolean state, UserDTO user) {
		this.state = state;
		this.user = user;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
