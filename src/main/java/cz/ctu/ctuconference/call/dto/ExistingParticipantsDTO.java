package cz.ctu.ctuconference.call.dto;

import java.util.List;

/**
 * Created by Nick nemame on 24.12.2016.
 */
public class ExistingParticipantsDTO {
	List<ParticipantInfoDTO> participantInfoList;
	ParticipantInfoDTO myInfo;

	public ExistingParticipantsDTO(List<ParticipantInfoDTO> participantInfoList, ParticipantInfoDTO myInfo) {
		this.participantInfoList = participantInfoList;
		this.myInfo = myInfo;
	}
	public List<ParticipantInfoDTO> getParticipantInfoList() {
		return participantInfoList;
	}

	public void setParticipantInfoList(List<ParticipantInfoDTO> participantInfoList) {
		this.participantInfoList = participantInfoList;
	}

	public ParticipantInfoDTO getMyInfo() {
		return myInfo;
	}

	public void setMyInfo(ParticipantInfoDTO myInfo) {
		this.myInfo = myInfo;
	}
}
