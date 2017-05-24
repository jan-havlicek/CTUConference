/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.authentication;

import cz.ctu.ctuconference.user.AppUser;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nick nemame
 */
public interface AuthenticationService {
	/**
	 * Provadi prihlaseni uzivatele
	 * @param email
	 * @param password
	 * @return
	 */
	@Transactional(readOnly = true)
	boolean login(String email, String password);

	/**
	 *
	 */
	@Transactional(readOnly = true)
	void logout();

	/**
	 * Logs in the currently registered user
	 * @param user
	 */
	void loginRegistered(AppUser user);

	AppUser getLoggedUser();
}
