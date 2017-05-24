package cz.ctu.ctuconference.group;

import com.google.gson.JsonObject;
import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MemberPrivilege;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.service.comm.GroupCommService;
import cz.ctu.ctuconference.user.UserSession;
import cz.ctu.ctuconference.utils.communication.AppMessageReceiver;
import cz.ctu.ctuconference.utils.communication.MessageReceiver;
import cz.ctu.ctuconference.utils.communication.WSMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * Created by Nick nemame on 29.12.2016.
 */
@Component
public class GroupMessageReceiver extends AppMessageReceiver implements MessageReceiver {

	@Autowired
	GroupCommService groupCommService;

	@Override
	public void handleMessage(String messageType, JsonObject jsonMessageData, WebSocketSession session) throws WSMessageException, IOException, Exception {
		switch(messageType) {
			case "group.suggest":
				suggestGroups(jsonMessageData, session);
				break;
			case "group.remove":
				removeGroup(jsonMessageData, session);
				break;
			case "group.create":
				createGroup(jsonMessageData, session);
				break;
			case "group.request": //client request contact list
				requestMembership(jsonMessageData, session);
				break;
			case "group.accept":
				acceptRequest(jsonMessageData, session);
				break;
			case "group.reject":
				rejectRequest(jsonMessageData, session);
				break;
			case "group.leave":
				leaveGroup(jsonMessageData, session);
				break;
			case "group.remove-membership":
				removeMembership(jsonMessageData, session);
				break;
			case "group.change-role":
				changeRole(jsonMessageData, session);
				break;
			case "group.memberships":
				getMemberships(jsonMessageData, session);
				break;
		}
	}

	@Override
	public String getListenedMessagePrefix() {
		return "group";
	}

	private void suggestGroups(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession userSession = getUserSession(session);
		groupCommService.suggestGroups(userSession);
	}

	private void createGroup(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession creatorSession = getUserSession(session);
		final String groupName = parser.getStringParameter(params, "name");
		GroupType groupType = GroupType.fromNumber(parser.getLongParameter(params, "type"));
		groupCommService.createGroup(creatorSession, groupName, groupType);
	}

	private void removeGroup(JsonObject params, WebSocketSession session) throws IOException, WSMessageException {
		UserSession creatorSession = getUserSession(session);
		final long groupId = parser.getLongParameter(params, "groupId");
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		groupCommService.removeGroup(creatorSession, groupId);
	}

	private void getMemberships(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		UserSession userSession = getUserSession(session);
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		groupCommService.getMemberships(userSession, groupId);
	}

	private void requestMembership(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		UserSession userSession = getUserSession(session);
		groupCommService.requestMembership(userSession, groupId);
	}

	private void acceptRequest(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		final long userId = parser.getLongParameter(params, "userId");
		//final MembershipRole role = MembershipRole.fromNumber(parser.getLongParameter(params, "role"));
		MembershipRole role = MembershipRole.ADMIN; // @todo dat toto pryc, pridelovat lepe role
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		UserSession adminSession = getUserSession(session);
		groupCommService.acceptMembership(adminSession, userId, groupId, role);
	}

	private void rejectRequest(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		final long userId = parser.getLongParameter(params, "userId");
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		UserSession adminSession = getUserSession(session);
		groupCommService.rejectMembership(adminSession, userId, groupId);
	}

	private void removeMembership(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		final long userId = parser.getLongParameter(params, "userId");
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		UserSession adminSession = getUserSession(session);
		groupCommService.removeMembership(adminSession, userId, groupId);
	}

	private void changeRole(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		final long userId = parser.getLongParameter(params, "userId");
		final MembershipRole role = MembershipRole.fromNumber(parser.getLongParameter(params, "role"));
		authorizeGroupAccess(groupId, session, MemberPrivilege.ADMINISTRATION);
		UserSession adminSession = getUserSession(session);
		groupCommService.changeRole(adminSession, userId, groupId, role);
	}

	private void leaveGroup(JsonObject params, WebSocketSession session) throws WSMessageException, IOException {
		final long groupId = parser.getLongParameter(params, "groupId");
		authorizeGroupAccess(groupId, session, MemberPrivilege.BASIC);
		UserSession memberSession = getUserSession(session);
		groupCommService.leaveGroup(memberSession, groupId);
	}
}
