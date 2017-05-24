package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.contact.dto.*;
import cz.ctu.ctuconference.conversation.service.ConversationConverter;
import cz.ctu.ctuconference.conversation.dto.ConversationEntityDTO;
import cz.ctu.ctuconference.friendship.dto.FriendDTO;
import cz.ctu.ctuconference.friendship.domain.Friendship;
import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.domain.GroupEvent;
import cz.ctu.ctuconference.group.domain.GroupTopic;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.dto.GroupDTO;
import cz.ctu.ctuconference.group.dto.GroupEventDTO;
import cz.ctu.ctuconference.group.dto.GroupTopicDTO;
import cz.ctu.ctuconference.group.service.dao.GroupDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick Nemame on 25.09.2016.
 */
@Service
public class ContactListServiceImpl implements ContactListService {

	@Autowired
	private ContactService contactService;

	@Autowired
	private ConversationConverter conversationConverter;

	@Autowired
	private ContactActivityService contactActivityService;

	@Autowired
	private GroupDAO groupDAO;

	/**
	 * Prepare contact list structure
	 * @param userId
	 * @return
	 */
	@Override
	public ContactList getContactList(long userId) {
			List<ContactListContainer> contactListGroups = new ArrayList<>();
		contactListGroups.add(getFriendList(userId));
		// contactListGroups.add(getMultichatList(userId));
		contactListGroups.addAll(getGroupList(userId));
		ContactList contactList = new ContactList(contactListGroups);
		return contactList;
	}

	public ContactListContainerGroup getGroupContainer(long groupId, long receiverId) {
		Group group = groupDAO.getById(groupId);
		return getGroupContainer(group, receiverId);
	}

	@Override
	public ContactListItemPrivate getFriendItem(Long friendId) {
		// getFriendList(friendId).getItems().stream().filter(item -> );
		// @todo get friend item (maybe it is necessary to have two params - user and friend
		return null;
	}

	/**
	 * Load fried list of the user. It creates contact list container and create contact list item for each friend.
	 * Then it loads activity information.
	 * @param userId
	 * @return
	 */
	private ContactListContainer getFriendList(long userId) {
		ContactListContainer friendListGroup = new ContactListContainer(ContactListContainer.TYPE_FRIENDLIST);
		List<Friendship> friendList = contactService.getFriendshipList(userId);

		for(Friendship friendship : friendList) {
			if(friendship.getState() != ContactAuthState.ACCEPTED) continue;
			FriendDTO friendDTO = new FriendDTO(
					friendship.getFriend().getId(),
					friendship.getFriend().getFullName(),
					conversationConverter.toDTO(friendship.getConversation())
			);
			_removeMeAsParticipant(friendDTO, userId);
			contactActivityService.augmentActivityInfo(friendDTO, false);
			ContactListItemPrivate contactListItem = new ContactListItemPrivate(friendDTO);
			friendListGroup.addItem(contactListItem);
		}
		return friendListGroup;
	}

/*
@todo implement multichat
	private ContactListContainer getMultichatList(long userId) {
		ContactListContainer multiConversationListGroup = new ContactListContainer(ContactListContainer.TYPE_MULTICHAT);
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
	}
*/
	private List<ContactListContainer> getGroupList(long userId) {
		List<ContactListContainer> groupContactList = new ArrayList<>();
		List<Group> groupList = contactService.getGroupList(userId);
		for(Group group : groupList) {
			ContactListContainerGroup groupContainer = getGroupContainer(group, userId);
			groupContactList.add(groupContainer);
		}
		return groupContactList;
	}

	/**
	 * Returns group container. It is necessary to get information about the user, that requested
	 * contact list. It is necessary to load their role in the group and info whether they are
	 * the only active administrator (The last administrator should not have permission to leave
	 * the group - just to remove it entirely.)
	 * @param group
	 * @param userId
	 * @return
	 */
	private ContactListContainerGroup getGroupContainer(Group group, long userId) {
		GroupDTO groupDTO = new GroupDTO(
				group.getId(),
				group.getName(),
				group.getType(),
				conversationConverter.toDTO(group.getConversation())
		);
		_removeMeAsParticipant(groupDTO, userId);
		contactActivityService.augmentActivityInfo(groupDTO, true);
		MembershipRole role = group.getMembershipList().stream()
				.filter(item -> item.getUser().getId() == userId)
				.collect(Collectors.toList()).get(0)
				.getRole();
		boolean isNotOnlyAdmin = role == MembershipRole.ADMIN && group.getMembershipList().stream().filter(membership ->
			membership.getRole() == MembershipRole.ADMIN && membership.isAccepted() && membership.getUser().getId() != userId
		).count() > 0;
		ContactListContainerGroup groupContainer = new ContactListContainerGroup(groupDTO, role, !isNotOnlyAdmin);
		groupContainer.setItems(getGroupItemList(group, userId));
		return groupContainer;
	}

	private List<ContactListItem> getGroupItemList(Group group, long userId) {
		List<ContactListItem> groupItemList = new ArrayList<>();
		for(GroupEvent event : group.getEventList()) {
			GroupEventDTO groupEventDTO = new GroupEventDTO(
					event.getId(),
					event.getName(),
					conversationConverter.toDTO(event.getConversation()),
					event.getFrom(),
					event.getTo()
			);
			_removeMeAsParticipant(groupEventDTO, userId);
			contactActivityService.augmentActivityInfo(groupEventDTO, true);
			ContactListItemGroupEvent item = new ContactListItemGroupEvent(groupEventDTO);
			groupItemList.add(item);
		}
		for(GroupTopic topic : group.getTopicList()) {
			GroupTopicDTO groupTopicDTO = new GroupTopicDTO(
					topic.getId(),
					topic.getName(),
					conversationConverter.toDTO(topic.getConversation())
			);
			_removeMeAsParticipant(groupTopicDTO, userId);
			contactActivityService.augmentActivityInfo(groupTopicDTO, true);
			ContactListItemGroupTopic item = new ContactListItemGroupTopic(groupTopicDTO);
			groupItemList.add(item);
		}
		return groupItemList;
	}

	private void _removeMeAsParticipant(ConversationEntityDTO conversationEntity, long userId) {
		conversationEntity.getConversation().getParticipantList()
				.removeIf(user -> user.getId() == userId);
	}
}
