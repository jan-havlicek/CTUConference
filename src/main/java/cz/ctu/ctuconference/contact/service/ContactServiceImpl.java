package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.conversation.domain.MultichatConversation;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.friendship.dto.FriendDTO;
import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.domain.GroupEvent;
import cz.ctu.ctuconference.group.domain.GroupTopic;
import cz.ctu.ctuconference.group.dto.GroupDTO;
import cz.ctu.ctuconference.group.service.dao.GroupDAO;
import cz.ctu.ctuconference.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 19.11.2016.
 */
@Service
public class ContactServiceImpl implements ContactService {

	/**
	 *
	 */
	@Autowired
	UserDAO userDAO;

	@Autowired
	GroupDAO groupDAO;

	public List<Friendship> getFriendshipList(long userId) {
		List<Friendship> friendList = userDAO.getFriendshipList(userId);
		return friendList;
	}

	public List<Group> getGroupList(long userId) {
		List<Group> groupList = userDAO.getGroupList(userId);
		return groupList;
	}


	public List<MultichatConversation> getMultichatList(long userId) {
		return null;
		/*ContactListContainer multiConversationListGroup = new ContactListContainer(ContactListContainer.TYPE_MULTICHAT);
		List<MultichatConversation> multiConversationList = userDAO.getById(userId).getMultichatList();
		for(MultichatConversation conversation : multiConversationList) {
			String conversationName, conversationTitle;
			if(conversation.getValue() != null) {
				conversationName = conversationTitle = conversation.getValue();
			} else {
				conversationName = conversation.getDefaultName();
				conversationTitle = conversation.getDefaultTitle();
			}
			ContactListItemMulti item = new ContactListItemMulti(
					conversation.getId(), conversationName, conversationTitle);
			multiConversationListGroup.addItem(item);
		}
		return multiConversationListGroup;
		*/
	}

	@Override
	public List<GroupTopic> getGroupTopicList(long userId) {
		List<GroupTopic> groupTopicList = userDAO.getGroupTopicList(userId);
		return groupTopicList;
	}

	@Override
	public List<GroupEvent> getGroupEventList(long userId) {
		List<GroupEvent> groupEventList = userDAO.getGroupEventList(userId);
		return groupEventList;
	}

	@Override
	public List<ConversationEntityDTO> filterContacts(long userId, String filter) {
		List<ConversationEntityDTO> filteredContacts = new ArrayList<>();

		List<GroupDTO> groupList = groupDAO.getByMatch(userId, filter).stream().map(group ->
				new GroupDTO(group.getId(), group.getName(), group.getType(), null)).collect(Collectors.toList());
		filteredContacts.addAll(groupList);

		List<FriendDTO> userList = userDAO.getByMatch(userId, filter).stream().map(user ->
				new FriendDTO(user.getId(), user.getFullName(), null)).collect(Collectors.toList());
		filteredContacts.addAll(userList);

		return filteredContacts;
	}
}
