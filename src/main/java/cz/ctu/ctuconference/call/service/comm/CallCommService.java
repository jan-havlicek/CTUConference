package cz.ctu.ctuconference.call.service.comm;

import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.user.UserSession;
import org.kurento.client.IceCandidate;

import java.io.IOException;

/**
 * Created by Nick nemame on 04.01.2017.
 */
public interface CallCommService {

	//void inviteMembersToCall(Call call) throws IOException;

	/**
	 * If the websocket session is closed, it is necessary to do some stuff.
	 * If there is call performed, it is necessary to leave it, if it is
	 * group call or close the call if there is not enough participants
	 * to continue the call or if the call type is webinar and the user is its initiator.
	 * @param userSession
	 * @throws IOException
	 */
	void handleUserSessionClose(UserSession userSession) throws IOException;

	/**
	 * Leave call, if it is necessary, it will close whole call (when initiator of
	 * webinar leaves or when the last caller of group call left).
	 * @param userSession
	 * @throws IOException
	 */
	void leaveCall(UserSession userSession) throws IOException;

	/**
	 * Invokes new call. It will send invitation to active group members and if
	 * the type of call is webinar, it will start the call immediately and the initiator
	 * will be joined to the call.
	 * @param initiatorSession
	 * @param conversationId
	 * @param byName
	 * @throws IOException
	 */
	void invokeCall(UserSession initiatorSession, long conversationId, CallType byName) throws IOException;

	/**
	 * It will join the user to the call.
	 * @param conversationId
	 * @param userSession
	 * @throws IOException
	 */
	void joinCall(long conversationId, UserSession userSession) throws IOException;

	void cancelCall(UserSession userSession) throws Exception;

	void rejectCall(long conversationId, UserSession userSession) throws IOException;

	void receiveVideoFrom(UserSession userSession, long senderId, String sdpOffer) throws IOException;

	void handleIceCandidate(UserSession userSession, long participantId, IceCandidate candidate);

	void modifyParticipant(UserSession lectorSession, ParticipantInfo participantInfo) throws IOException;
}
