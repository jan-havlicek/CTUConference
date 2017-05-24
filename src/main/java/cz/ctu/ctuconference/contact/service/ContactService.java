package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.conversation.domain.MultichatConversation;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.domain.GroupEvent;
import cz.ctu.ctuconference.group.domain.GroupTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nick nemame on 19.11.2016.
 */
@Service
public interface ContactService {

	@Transactional(readOnly = true)
	List<Friendship> getFriendshipList(long userId);

	@Transactional(readOnly = true)
	List<MultichatConversation> getMultichatList(long userId);

	@Transactional(readOnly = true)
	List<Group> getGroupList(long userId);

	@Transactional(readOnly = true)
	List<GroupTopic> getGroupTopicList(long userId);

	@Transactional(readOnly = true)
	List<GroupEvent> getGroupEventList(long userId);

	@Transactional(readOnly = true)
	List<ConversationEntityDTO> filterContacts(long userId, String filter);

}
