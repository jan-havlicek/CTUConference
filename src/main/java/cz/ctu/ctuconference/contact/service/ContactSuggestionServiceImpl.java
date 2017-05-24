package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.friendship.dto.FriendDTO;
import cz.ctu.ctuconference.group.service.dao.GroupDAO;
import cz.ctu.ctuconference.group.dto.GroupDTO;
import cz.ctu.ctuconference.group.dto.GroupEventDTO;
import cz.ctu.ctuconference.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 26.11.2016.
 */
@Service
public class ContactSuggestionServiceImpl implements ContactSuggestionService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Override
	public List<FriendDTO> getFriendSuggestions(long userId) {
		return userDAO.suggestFriendsToUser(userId).stream().map(user -> new FriendDTO(user.getId(), user.getFullName(), null)).collect(Collectors.toList());
	}

	@Override
	public List<GroupDTO> getGroupSuggestions(long userId) {
		return groupDAO.suggestGroupsToUser(userId).stream().map(group -> new GroupDTO(group.getId(), group.getName(), group.getType(), null)).collect(Collectors.toList());
	}

	@Override
	public List<GroupEventDTO> getGroupEventSuggestions(long userId) {
		return null;
		//@todo implement
	}

	@Override
	public List<GroupEventDTO> getEventSuggestion(long userId) {
		return null;
		//@todo implement
	}
}
