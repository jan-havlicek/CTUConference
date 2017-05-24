/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */
package cz.ctu.ctuconference.user;

import cz.ctu.ctuconference.call.domain.ParticipantInfo;
import cz.ctu.ctuconference.call.domain.ParticipantType;
import cz.ctu.ctuconference.call.domain.UserCall;

import java.io.Closeable;
import java.io.IOException;

import cz.ctu.ctuconference.utils.communication.AppMessageSender;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.web.socket.WebSocketSession;

/**
 * Represents session for authorized user. When user is logged into
 * the application, UserSession will be initialized (it doesn't contain
 * any WebRTC connection now). Then they can initialize call and in this
 * time WebRTC connections will be set.
 *
 * Author of example
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class UserSession implements Closeable {

	/**
	 * websocket session - set when UserSession is created
	 */
    private final WebSocketSession session;

	/**
	 * Identifier of the user authorized to this websocket session
	 */
    private final long userId;

	/**
	 * Name of the user authorized to this websocket session
	 */
	private final String userName;
    private final AppMessageSender messageSender;

	/**
	 * Set when the user is joined to the call and cleared when it is finished.
	 * It contains media specific data
	 */
    private UserCall userCall;

    /**
     * Creates user session containing information about user and
     * (WebSocket) session. Outgoing and incoming media is currently not set (it
     * is created when tolling user, so it is not necessary to create WebRTC
     * media yet.
     *
     * @param userId
     * @param messageSender
     * @param session
     */
    public UserSession(final long userId, final String userName, final WebSocketSession session, final AppMessageSender messageSender) {

        this.userId = userId;
		this.userName = userName;
	    this.messageSender = messageSender;
        this.session = session;
    }

    /* User call handling */

    /**
     * When the call is initialized and the user join it, the UserCall instance
     * is created.
     * @param conversationId
     * @param mediaPipeline
     */
    public void initCall(long conversationId, ParticipantInfo participantInfo, MediaPipeline mediaPipeline) {
        userCall = new UserCall(this, conversationId, mediaPipeline, participantInfo, messageSender);
    }

    public void endCall() throws IOException {
		userCall.close();
		userCall = null;
	}

	/**
	 * Returns information whether the user is in call or not.
	 * @return
	 */
	public boolean isInCall() {
        return userCall != null;
    }

    public UserCall getUserCall() {
		return userCall;
	}

	public boolean isTransmitting() {
		return this.userCall.getParticipantInfo().isTransmitting();
	}

//
//	public void connectToOutgoingEndpoint(WebRtcEndpoint incoming) {
//		userCall.connectToOutgoingEndpoint(incoming);
//	}

//    public void receiveVideoFrom(UserSession sender, String sdpOffer) throws IOException {
//        userCall.receiveVideoFrom(sender, sdpOffer);
//    }
//
//    public void cancelVideoFrom(long senderId) {
//        userCall.cancelVideoFrom(senderId);
//    }
//
//    public WebRtcEndpoint getOutgoingMediaEndpoint() {
//        return userCall.getOutgoingMediaEndpoint();
//    }
//
//    public void addCandidate(IceCandidate candidate, long userId) {
//        userCall.addCandidate(candidate, userId);
//    }

    /* Getters */

    public long getUserId() {
        return userId;
    }

	public String getUserName() {
		return userName;
	}

	public WebSocketSession getSession() {
        return session;
    }

    @Override
    public void close() throws IOException {
        if(userCall != null) {
			endCall();
		}
    }

    /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) obj;
        boolean eq = userId == other.getUserId();
        return eq;
    }

    /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + ((Long) userId).hashCode();
        //result = 31 * result + ((Long) conversationId).hashCode();
        return result;
    }

}
