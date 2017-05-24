/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.authentication;

import cz.ctu.ctuconference.user.AppUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import cz.ctu.ctuconference.user.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nick nemame
 */
@Component
public class AuthenticationServiceImpl implements AuthenticationService, Serializable {

    private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(UserSession.class);

	@Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Override
	public boolean login(String email, String password) {
		try {
			Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			if (authenticate.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authenticate);
				return true;
			}
		} catch (AuthenticationException e) {
		}
		return false;
	}

	@Override
	public void logout() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}

	/**
	 * Logs in the currently registered user. There is not necessary to
	 * do the authentication, becaouse the credentials are already trusted
	 * and stored into database.
	 * @param user
	 */
	public void loginRegistered(AppUser user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_LOGGED_USER"));
		Authentication authenticate = new UsernamePasswordAuthenticationToken(
				user,
				"registered",
				authorities);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
	}


    @Override
    public AppUser getLoggedUser() {
 	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication.getPrincipal().equals("anonymousUser")) return null;
        return (AppUser) authentication.getPrincipal();
    }

}
