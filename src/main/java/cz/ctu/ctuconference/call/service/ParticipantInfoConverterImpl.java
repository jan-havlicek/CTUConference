package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.call.dto.ParticipantInfoDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 05.01.2017.
 */
@Service
public class ParticipantInfoConverterImpl implements ParticipantInfoConverter {
	@Override
	public ParticipantInfoDTO toDTO(ParticipantInfo participantInfo) {
		return new ParticipantInfoDTO(
				participantInfo.getId(),
				participantInfo.isVideoMuted(),
				participantInfo.isAudioMuted(),
				participantInfo.isInitiator(),
				participantInfo.getParticipantType());
	}

	@Override
	public List<ParticipantInfoDTO> toDTOList(List<ParticipantInfo> participantInfoList) {
		return participantInfoList.stream().map(participantInfo -> toDTO(participantInfo)).collect(Collectors.toList());
	}
}
