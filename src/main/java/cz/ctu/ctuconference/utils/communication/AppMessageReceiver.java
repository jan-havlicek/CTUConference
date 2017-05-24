package cz.ctu.ctuconference.utils.communication;

import cz.ctu.ctuconference.call.domain.ParticipantType;
import cz.ctu.ctuconference.call.service.CallManager;
import cz.ctu.ctuconference.conversation.service.ConversationService;
import cz.ctu.ctuconference.group.domain.MemberPrivilege;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.service.GroupService;
import cz.ctu.ctuconference.user.UserRegistry;
import cz.ctu.ctuconference.user.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

/**
 * Created by Nick nemame on 27.12.2016.
 */
public abstract class AppMessageReceiver {

	@Autowired
	private UserRegistry registry;

	@Autowired
	private ConversationService conversationService;

	@Autowired
	private GroupService groupService;

	@Autowired
	protected ISocketMessageDataParser parser;

	@Autowired
	protected CallManager callManager;

	protected UserSession getUserSession(WebSocketSession session) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession == null) throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
		return userSession;
	}

	protected long getUserId(WebSocketSession session) throws WSMessageException {
		UserSession userSession = getUserSession(session);
		return userSession.getUserId();
	}

	protected boolean authorizeConversationAccess(long conversationId, WebSocketSession session) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession != null) {
			if(conversationService.isMember(conversationId, userSession.getUserId())) {
				return true;
			}
		}
		throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
	}

	/**
	 * Authorizes user access to the group. Group must be active and user must have privileges.
	 * @param groupId
	 * @param session
	 * @param privilege
	 * @return
	 * @throws WSMessageException
	 */
	protected boolean authorizeGroupAccess(long groupId, WebSocketSession session, MemberPrivilege privilege) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession != null) {
			long userId = userSession.getUserId();
			if(!groupService.isActive(groupId)) throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
			switch(privilege) {
				case ADMINISTRATION:
					return groupService.hasAdminPermissions(userId, groupId);
				case CALL_PERFORMING:
					return groupService.isMember(userId, groupId)
						&& (groupService.hasLectorPermissions(userId, groupId)
							|| groupService.getMembershipRole(userId, groupId) == MembershipRole.TEAM_MEMBER);
				case CALL_LISTENING:
					return groupService.isMember(userId, groupId);
				case BASIC:
					return groupService.isMember(userId, groupId);
			}
		}
		throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
	}

	/**
	 * Authorize only for call that he is a participant
	 * @param conversationId
	 * @param session
	 * @return
	 */
	protected boolean authorizeCallAccess(long conversationId, WebSocketSession session) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession != null) {
			if(conversationService.isMember(conversationId, userSession.getUserId())
					&& userSession.isInCall()
					&& userSession.getUserCall().getConversationId() == conversationId) {
				return true;
			}
		}
		throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
	}

	/**
	 * Authorize only for call that he is a member
	 * @param session
	 * @return
	 */
	protected boolean authorizeCallInitiatorAccess(WebSocketSession session) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession != null && userSession.isInCall()) {
			long conversationId = userSession.getUserCall().getConversationId();
			if(callManager.getCall(conversationId).getInitiatorId() == userSession.getUserId()) {
				return true;
			}

		}
		throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
	}


	protected boolean authorizeCallAccessAs(WebSocketSession session, ParticipantType participantType) throws WSMessageException {
		UserSession userSession = registry.getBySession(session);
		if(userSession != null && userSession.isInCall()) {
			if(userSession.getUserCall().getParticipantInfo().getParticipantType() == participantType) {
				return true;
			}

		}
		throw new WSMessageException(WSMessageException.NOT_AUTHORIZED);
	}

}
