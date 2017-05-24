/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Nick nemame
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

/*
    private void storeUserPassword(AppUser userEntity, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        userEntity.setPassword(password);
    }
*/

	@Override
	public AppUser getById(long userId) {
		return userDAO.getById(userId);
	}

	@Override
    public boolean existsWithEmail(String email) {
        return userDAO.getOneByEmail(email) != null;
    }

    @Override
    public boolean exists(long id) {
        return userDAO.exists(id);
    }
}
