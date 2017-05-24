package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;
import org.springframework.stereotype.Service;

/**
 * Created by Nick nemame on 04.01.2017.
 */
@Service
public class CallConverterImpl implements CallConverter {
	@Override
	public CallInfoDTO toDTO(Call call) {
		return new CallInfoDTO(call.getInitiatorId(), call.getInitiatorName(), call.getConversationId(), call.getCallName(), call.getCallType());
	}
}
