package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.call.dto.ParticipantInfoDTO;

import java.util.List;

/**
 * Created by Nick nemame on 05.01.2017.
 */
public interface ParticipantInfoConverter {

	ParticipantInfoDTO toDTO(ParticipantInfo participantInfo);

	List<ParticipantInfoDTO> toDTOList(List<ParticipantInfo> participantInfoList);
}
