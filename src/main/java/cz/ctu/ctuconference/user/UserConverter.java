package cz.ctu.ctuconference.user;

import cz.ctu.ctuconference.conversation.dto.ParticipantDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick nemame on 15.11.2016.
 */
@Component
public class UserConverter {

	public List<Long> toIdList(List<AppUser> userList) {
		List<Long> idList = new ArrayList<>();
		userList.forEach((user) -> idList.add(user.getId()));
		return idList;
	}

	public List<ParticipantDTO> toDTOList(List<AppUser> userList) {
		List<ParticipantDTO> dtoList = new ArrayList<>();
		userList.forEach((user) -> dtoList.add(toDTO(user)));
		return dtoList;
	}

	public ParticipantDTO toDTO(AppUser user) {
		ParticipantDTO participantDTO = new ParticipantDTO(
				user.getId(),
				user.getFirstName(),
				user.getLastName()
		);
		return participantDTO;
	}

}
