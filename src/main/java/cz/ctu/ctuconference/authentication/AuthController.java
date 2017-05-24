package cz.ctu.ctuconference.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
	import cz.ctu.ctuconference.authentication.dto.LoginDTO;
import cz.ctu.ctuconference.registration.RegistrationDTO;
import cz.ctu.ctuconference.registration.RegistrationService;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserDTO;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.PersistenceException;
import java.util.UUID;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private AuthTokenCache authTokenCache;

	private Gson gson;

	public AuthController() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gson = gsonBuilder.create();
	}

	/**
	 *
	 * @param auth
	 * @return
	 */
	@RequestMapping(path = "/login", method = RequestMethod.POST,
			consumes = {"application/json"})
	public ResponseEntity<String> loginAction(@RequestBody LoginDTO auth) {
		boolean isLoginSuccessful;
		try {
			isLoginSuccessful = authenticationService.login(auth.getLogin(), auth.getPassword());
			if(isLoginSuccessful) {
				AppUser appUser = authenticationService.getLoggedUser();
				UUID token = UUID.randomUUID();
				UserDTO userDTO = new UserDTO(appUser.getId(), appUser.getEmail(), appUser.getFirstName(), appUser.getLastName(), token.toString());
				authTokenCache.addAuthToken(token, appUser);
				return ResponseEntity.ok(gson.toJsonTree(userDTO).toString());
			}
		} catch(CannotCreateTransactionException | PersistenceException | DatabaseException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}

	/**
	 *
	 * @param registration
	 * @return
	 */
	@RequestMapping(path = "/register", method = RequestMethod.POST,
			consumes = {"application/json"})
	public ResponseEntity<String> registerAction(@RequestBody RegistrationDTO registration) {
		try {
			registrationService.register(registration);
			AppUser appUser = authenticationService.getLoggedUser();
			UUID token = UUID.randomUUID();
			UserDTO userDTO = new UserDTO(appUser.getId(), appUser.getEmail(), appUser.getFirstName(), appUser.getLastName(), token.toString());
			authTokenCache.addAuthToken(token, appUser);
			return ResponseEntity.ok(gson.toJsonTree(userDTO).toString());
		} catch(CannotCreateTransactionException | PersistenceException | DatabaseException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MUJ POKUS");
		}
	}

	/**
	 *
	 * @return
	 */
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
	public ResponseEntity<String> logoutAction() {
		authenticationService.logout();
		JsonObject messageData = new JsonObject();
		messageData.addProperty("state", "ok");
		return ResponseEntity.ok(messageData.toString());
	}

	/**
	 * Serves new token for user
	 * @return
	 */
	@RequestMapping(path = "/token", method = RequestMethod.GET)
	public ResponseEntity<String> requestTokenAction() {
		AppUser appUser = authenticationService.getLoggedUser();
		if(appUser == null) {
			return ResponseEntity.ok("");
		}
		UUID token = UUID.randomUUID();
		JsonObject json = new JsonObject();
		json.addProperty("token", token.toString());
		authTokenCache.addAuthToken(token, appUser);
		return ResponseEntity.ok(json.toString());
	}
}
