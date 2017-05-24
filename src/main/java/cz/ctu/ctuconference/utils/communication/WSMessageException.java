/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.utils.communication;

/**
 *
 * @author Nick Nemame
 */
public class WSMessageException extends Exception {

	public static final String UNKNOWN_TYPE = "Message type in 'type' property is not recognised";
	public static final String TYPE_MISSING = "Message 'type' property is missing";
	public static final String DATA_MISSING = "Message 'data' property is missing";
	public static final String NOT_IN_CALL = "There is no call for requested conversation";
	public static final String NOT_AUTHORIZED = "You don't have the permission to access this conversation";

    public WSMessageException(String message) {
        super(message);
    }

}
