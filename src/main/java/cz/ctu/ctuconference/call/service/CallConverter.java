package cz.ctu.ctuconference.call.service;

import cz.ctu.ctuconference.call.domain.Call;
import cz.ctu.ctuconference.call.dto.CallInfoDTO;

/**
 * Created by Nick nemame on 04.01.2017.
 */
public interface CallConverter {

	CallInfoDTO toDTO(Call call);
}
