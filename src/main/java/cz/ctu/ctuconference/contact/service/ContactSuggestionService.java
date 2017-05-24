package cz.ctu.ctuconference.contact.service;

import cz.ctu.ctuconference.friendship.dto.FriendDTO;
import cz.ctu.ctuconference.group.dto.GroupDTO;
import cz.ctu.ctuconference.group.dto.GroupEventDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nick nemame on 26.11.2016.
 */
@Service
public interface ContactSuggestionService {

	@Transactional(readOnly = true)
	List<FriendDTO> getFriendSuggestions(long userId);

	@Transactional(readOnly = true)
	List<GroupDTO> getGroupSuggestions(long userId);

	@Transactional(readOnly = true)
	List<GroupEventDTO> getGroupEventSuggestions(long userId);

	@Transactional(readOnly = true)
	List<GroupEventDTO> getEventSuggestion(long userId);
}
