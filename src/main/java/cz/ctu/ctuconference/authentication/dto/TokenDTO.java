package cz.ctu.ctuconference.authentication.dto;

import java.util.UUID;

/**
 * Created by Nick nemame on 10.12.2016.
 */
public class TokenDTO {
	private String token;

	public TokenDTO() {
	}

	public TokenDTO(String token) {
		this.token = token;
	}

	public String getString() {
		return token;
	}

	public void setString(String token) {
		this.token = token;
	}

	public UUID getUUID() {
		return UUID.fromString(token);
	}

	public void setUUID(UUID tokenUUID) {
		this.token = tokenUUID.toString();
	}
}
