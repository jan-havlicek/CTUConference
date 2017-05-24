package cz.ctu.ctuconference.contact.service.comm;

import cz.ctu.ctuconference.call.domain.CallState;
import cz.ctu.ctuconference.call.domain.CallType;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;
import cz.ctu.ctuconference.contact.domain.ContactState;
import cz.ctu.ctuconference.contact.dto.ContactListContainerGroup;
import cz.ctu.ctuconference.contact.dto.ContactListItemPrivate;
import cz.ctu.ctuconference.conversation.dto.ParticipantDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public interface ContactStateNotifierService {

	@Transactional(readOnly = true)
	void notifyUserStateChanged(long userId, ContactState state) throws IOException;

	@Transactional(readOnly = true)
	void notifyUserInCall(long userId, long conversationId) throws IOException;

	@Transactional(readOnly = true)
	void notifyUserStopCalling(long userId) throws IOException;

	// void notifyFriendStateChanged(long friendId, ContactState state) throws IOException;

	@Transactional(readOnly = true)
	void notifyFriendAdded(ContactListItemPrivate friendItem) throws IOException;

	@Transactional(readOnly = true)
	void notifyFriendRemoved(long friendId) throws IOException;

	// void notifyGroupMemberStateChanged(long memberId, ContactState state) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupMemberAdded(ParticipantDTO member, long conversationId) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupMemberRemoved(long memberId, long conversationId) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupRemoved(long groupId) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupAdded(ContactListContainerGroup groupContainer, long receiverId) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupStateChanged(long groupId) throws IOException;

	@Transactional(readOnly = true)
	void notifyGroupCallState(long conversationId, CallState callState, CallType callType, CallInfoDTO callInfo) throws IOException;
}
