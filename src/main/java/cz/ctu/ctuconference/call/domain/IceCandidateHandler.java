package cz.ctu.ctuconference.call.domain;

import org.kurento.client.IceCandidate;

/**
 * Created by Nick nemame on 04.01.2017.
 */
public interface IceCandidateHandler {
	void onIceCandidate(long participantId, IceCandidate candidate);
}
