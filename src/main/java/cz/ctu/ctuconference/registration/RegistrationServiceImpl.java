/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.registration;

import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.authentication.AuthenticationService;
import cz.ctu.ctuconference.user.UserDAO;
import cz.ctu.ctuconference.user.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * @author Nick nemame
 */
@Component
public class RegistrationServiceImpl implements RegistrationService, Serializable {

    private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(UserSession.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private AuthenticationService authenticationService;

    @Override
	public void register(RegistrationDTO registration) {
		AppUser registeredUser = new AppUser(
				null,
				registration.getEmail(),
				registration.getFirstName(),
				registration.getLastName());
		registeredUser.setPassword(registration.getPassword());
		userDAO.storeRegistration(registeredUser);
		authenticationService.loginRegistered(registeredUser);
	}
}
