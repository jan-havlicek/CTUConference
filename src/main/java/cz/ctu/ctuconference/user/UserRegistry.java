/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */

package cz.ctu.ctuconference.user;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class UserRegistry {

	/**
	 * Active user sessions by user id
	 */
	private final ConcurrentHashMap<Long, UserSession> usersById = new ConcurrentHashMap<>();
	/**
	 * Active user sessions by session id
	 */
	private final ConcurrentHashMap<String, UserSession> usersBySessionId = new ConcurrentHashMap<>();

	/**
	 * Suspended user sessions by session id.
	 * It is still living session, but user is currently connected via another session.
	 * User needs to refresh page to restore this session
	 */
	// private final ConcurrentHashMap<String, UserSession> suspendedBySessionId = new ConcurrentHashMap<>();

	public void register(UserSession user) {
		usersById.put(user.getUserId(), user);
		usersBySessionId.put(user.getSession().getId(), user);
	}

	public UserSession getByUserId(long userId) {
		return usersById.get(userId);
	}

	public UserSession getBySession(WebSocketSession session) {
		return usersBySessionId.get(session.getId());
	}

	public boolean exists(long userId) {
		return usersById.keySet().contains(userId);
	}

	public UserSession removeBySession(WebSocketSession session) {
		final UserSession user = getBySession(session);
		usersById.remove(user.getUserId());
		usersBySessionId.remove(session.getId());
		return user;
	}

	/**
	 * Returns list of user sessions for all
	 * @param conversationId
	 * @return
	 */
	public List<UserSession> getUsersInCall(long conversationId) {
		return usersById.values().stream()
				.filter(userSession -> userSession.isInCall() && userSession.getUserCall().getConversationId() == conversationId)
				.collect(Collectors.toList());
	}
}
