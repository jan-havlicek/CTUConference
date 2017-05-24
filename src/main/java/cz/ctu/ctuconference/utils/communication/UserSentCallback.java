package cz.ctu.ctuconference.utils.communication;

import cz.ctu.ctuconference.user.UserSession;

/**
 * Created by Nick nemame on 26.12.2016.
 */
public interface UserSentCallback {
	void onEvent(UserSession userSession);
}
