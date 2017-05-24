/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.authentication;

import cz.ctu.ctuconference.user.UserDAO;
import cz.ctu.ctuconference.user.AppRole;
import cz.ctu.ctuconference.user.AppUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A custom authentication manager that allows access if the user details exist
 * in the database and if the username and password are not the same. Otherwise,
 * throw a {@link BadCredentialsException}
 */
@Service("authenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDAO userDAO;

    //@Autowired
    //private SaltGenerator saltGenerator;
    //@Autowired
    //private LoggedUser loggedUser;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        AppUser user = null;
        try {
			user = userDAO.getOneByEmail(auth.getName());
			if(user == null) throw new BadCredentialsException("Uživatel neexistuje");
        } catch (Exception e) {
            throw new BadCredentialsException("Uživatel neexistuje");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_LOGGED_USER"));
        return new UsernamePasswordAuthenticationToken(
                user,
                auth.getCredentials(),
                authorities);
/*
        // Make sure to encode the password first before comparing
        //System.out.println("GENERATE DYNAMIC SALT: " + saltGenerator.generateDynamicSalt(user.getSalt()) + " FROM: "+user.getSalt());
        if (passwordEncoder.matches(user.getPassword(), (String) auth.getCredentials()) == false) {
            throw new BadCredentialsException("Chybné heslo");
        }

        // Username and password must be the same to authenticate
        if (auth.getName().equals(auth.getCredentials()) == true) {
            throw new BadCredentialsException("Jméno a heslo nesmí být stejné!");

        }

        //loggedUser.init(user.getId(), user.getLogin(), user.getFirstName()+" "+user.getLastName());
        return new UsernamePasswordAuthenticationToken(
                auth.getName(),
                auth.getCredentials(),
                getAuthorities(user));*/
    }

    /**
     * Retrieves the correct ROLE type depending on the access level, where
     * access level is an Integer. Basically, this interprets the access value
     * whether it's for a regular user or admin.
     *
     * @param user
     * @return collection of granted authorities
     */
    public Collection<GrantedAuthority> getAuthorities(AppUser user) {
        // Create a list of grants for this user
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

        // All users are granted with ROLE_USER access
        // Therefore this user gets a ROLE_USER by default
        for (AppRole role : user.getRoleList()) {
            authList.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        // Return list of granted authorities
        return authList;
    }

    /*
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("Jsem v authentication manageru");
        userDAO.userList();
        return null;
    }
     */
    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
