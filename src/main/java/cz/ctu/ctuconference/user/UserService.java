/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.user;

import java.util.List;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Nick nemame
 */
@Component
public interface UserService {

	@Transactional(readOnly=true)
	AppUser getById(long userId);

    /**
     * Zkontroluje, zda jiz existuje uzivatel se jmenem
     * @param email
     * @return
     */
    @Transactional(readOnly=true)
    boolean existsWithEmail(String email);

    /**
     * Zkontroluje, zda uzivatel existuje
     * @param id
     * @return
     */
    @Transactional(readOnly=true)
    boolean exists(long id);

}
