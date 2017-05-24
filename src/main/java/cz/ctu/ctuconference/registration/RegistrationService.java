/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.registration;

import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nick nemame
 */
public interface RegistrationService {
	/**
	 * Provadi registraci uzivatele
	 * @param registration
	 * @return
	 */
	@Transactional
	void register(RegistrationDTO registration);

}
