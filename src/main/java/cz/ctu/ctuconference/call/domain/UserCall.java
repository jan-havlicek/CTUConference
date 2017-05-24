
package cz.ctu.ctuconference.call.domain;

import cz.ctu.ctuconference.call.dto.IceCandidateDTO;
import cz.ctu.ctuconference.user.UserSession;
import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.kurento.client.Continuation;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserCall class represents call that contains one outgoing stream and
 * multiple incoming stream in one active conversation.
 * @author Nick Nemame
 */
public class UserCall implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(UserSession.class);

    private AppMessageSender messageSender;
    private final UserSession userSession;
    private final long conversationId;
	/**
	 * Info of the participant - defines role in call and info whether user can transmit audio or video stream.
	 */
	private ParticipantInfo participantInfo;

	/**
	 * Kurento pipeline, which the participant stream is connected to.
	 */
    private final MediaPipeline pipeline;

	/**
	 * If user is able and allowed to broadcast audiovisual stream, it contains outgoing media endpoint.
	 */
    private WebRtcEndpoint outgoingMedia;

	/**
	 * List of incoming audiovisual media endpoints.
	 */
    private final ConcurrentMap<Long, WebRtcEndpoint> incomingMedia;

    public UserCall(final UserSession userSession, final long conversationId, MediaPipeline pipeline, ParticipantInfo participantInfo, final AppMessageSender messageSender) {
        this.userSession = userSession;
        this.conversationId = conversationId;
        this.pipeline = pipeline;
        this.messageSender = messageSender;
		this.incomingMedia = new ConcurrentHashMap<>();
		outgoingMedia = null;
		this.participantInfo = participantInfo;
		if(!participantInfo.isAudioMuted() || !participantInfo.isVideoMuted()) {
			startTransmitting();
		}

     }

    public void startTransmitting() {
		this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();
		this.outgoingMedia.addOnIceCandidateListener(event -> {
			IceCandidateDTO candidateDTO = new IceCandidateDTO(userSession.getUserId(), event.getCandidate());
			try {
				messageSender.send(userSession, "call.iceCandidate", candidateDTO);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
	}

	public WebRtcEndpoint getOutgoingMediaEndpoint() {
		return outgoingMedia;
	}

	/**
	 * For statistics testing purpose
	 * @return
	 */
	public Map<Long, WebRtcEndpoint> getIncomingMediaEndpoints() {
		return incomingMedia;
	}

	/**
	 * Get incoming WebRTC endpoint (to receive media from sender)
	 * @param senderSession
	 * @return
	 */
	private WebRtcEndpoint getIncomingMediaEndpoint(final UserSession senderSession) {
        if (senderSession.getUserId() == userSession.getUserId()) {
            log.debug("PARTICIPANT {}: configuring loopback", userSession.getUserId());
            return outgoingMedia;
        }

        log.debug("PARTICIPANT {}: receiving video from {}", userSession.getUserId(), senderSession.getUserId());

        WebRtcEndpoint incoming = incomingMedia.get(senderSession.getUserId());
        if (incoming == null) {
            log.debug("PARTICIPANT {}: creating new endpoint for {}", userSession.getUserId(), senderSession.getUserId());
            incoming = new WebRtcEndpoint.Builder(pipeline).build();

            incoming.addOnIceCandidateListener(event -> {
                IceCandidateDTO candidateDTO = new IceCandidateDTO(senderSession.getUserId(), event.getCandidate());
                try {
                    messageSender.send(userSession, "call.iceCandidate", candidateDTO);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            });

            incomingMedia.put(senderSession.getUserId(), incoming);
        }

        log.debug("PARTICIPANT {}: obtained endpoint for {}", userSession.getUserId(), senderSession.getUserId());
        senderSession.getUserCall().connectToOutgoingEndpoint(incoming);

        return incoming;
    }
	/**
	 * Incoming WebRTC endpoint of the receiving user is connected to the outgoing WebRTC endpoint of the sending user
	 * (whose session this is).
	 * @param incoming
	 */
	public void connectToOutgoingEndpoint(WebRtcEndpoint incoming) {
		getOutgoingMediaEndpoint().connect(incoming);
	}

	/**
	 * Process SDP offer and return the SDP answer
	 * @param sender
	 * @param sdpOffer
	 * @return
	 */
    public String getSdpAnswer(UserSession sender, String sdpOffer) {
		String ipSdpAnswer = getIncomingMediaEndpoint(sender).processOffer(sdpOffer);
		return ipSdpAnswer;
	}

	/**
	 * Gather candidates for the user, who is transmitting the media, that user wants to receive.
	 * @param sender
	 */
	public void gatherCandidates(UserSession sender) {
		getIncomingMediaEndpoint(sender).gatherCandidates();
	}
//
//    public void receiveVideoFrom(UserSession sender, String sdpOffer) throws IOException {
//		final String ipSdpAnswer = getIncomingMediaEndpoint(sender).processOffer(sdpOffer);
//		//messageSender.sendReceiveVideoAnswer(this, sender, ipSdpAnswer);
//		messageSender.send(userSession, "call.receiveVideoAnswer", new SDPAnswerDTO(sender.getUserId(), ipSdpAnswer));
//		getIncomingMediaEndpoint(sender).gatherCandidates();
//	}

	/**
	 * Release the incoming stream from the user who is transmitting the media (used when the transmitting user
	 * is leaving the call)
	 * @param senderId
	 */
    public void cancelVideoFrom(long senderId) {
		if(!incomingMedia.containsKey(senderId)) return;
        log.debug("PARTICIPANT {}: canceling video reception from {}", userSession.getUserId(), senderId);
        WebRtcEndpoint incoming = incomingMedia.remove(senderId);
        log.debug("PARTICIPANT {}: removing endpoint for {}", userSession.getUserId(), senderId);
        incoming.release(new Continuation<Void>() {
            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released successfully incoming EP for {}", userSession.getUserId(), senderId);
            }
            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release incoming EP for {}", userSession.getUserId(), senderId);
            }
        });
    }

	/**
	 * It will disconnect endpoint connected to this outgoing endpoint. It is necessary when user finish the call
	 * or if the user transmitted and their privilege is changed to only listening.
	 */
	public void stopTransmitting() {
		outgoingMedia.release(new Continuation<Void>() {
			@Override
			public void onSuccess(Void result) throws Exception {
				log.trace("PARTICIPANT {}: Released outgoing EP", userSession.getUserId());
			}
			@Override
			public void onError(Throwable cause) throws Exception {
				log.warn("USER {}: Could not release outgoing EP", userSession.getUserId());
			}
		});
	}

	/**
	 * It is called when user ended the call. It will release all incoming streams and also release outgoing media stream.
	 * @throws IOException
	 */
    @Override
    public void close() throws IOException {
        releaseIncomingMediaEndpoints();
        if(outgoingMedia != null) {
			stopTransmitting();
		}
    }

	/**
	 * Release incoming media endpoints - it can now happen only when user
	 * finishes, because user cannot be part of the call with just transmitting and not receiving the media.
	 */
	private void releaseIncomingMediaEndpoints() {
		log.debug("PARTICIPANT {}: Releasing resources", userSession.getUserId());
		for (final long remoteParticipantId : incomingMedia.keySet()) {
			log.trace("PARTICIPANT {}: Released incoming EP for {}", userSession.getUserId(), remoteParticipantId);
			final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantId); //@todo if it would be remove instead of get, it could replace with cancelVideoFrom
			ep.release(new Continuation<Void>() {
				@Override
				public void onSuccess(Void result) throws Exception {
					log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
							userSession.getUserId(), remoteParticipantId);
				}
				@Override
				public void onError(Throwable cause) throws Exception {
					log.warn("PARTICIPANT {}: Could not release incoming EP for {}", userSession.getUserId(),
							remoteParticipantId);
				}
			});
		}
	}


    public void addCandidate(IceCandidate candidate, long userId) {
        if (userSession.getUserId() == userId) {
            outgoingMedia.addIceCandidate(candidate);
        } else {
            WebRtcEndpoint webRtc = incomingMedia.get(userId);
            if (webRtc != null) {
                webRtc.addIceCandidate(candidate);
            }
        }
    }

    public long getConversationId() {
        return conversationId;
    }

	public ParticipantInfo getParticipantInfo() {
		return participantInfo;
	}

	public void setParticipantInfo(ParticipantInfo participantInfo) {
		this.participantInfo = participantInfo;
	}

}
