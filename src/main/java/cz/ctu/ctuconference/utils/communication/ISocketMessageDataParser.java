package cz.ctu.ctuconference.utils.communication;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Nick Nemame on 04.10.2016.
 */
public interface ISocketMessageDataParser {

	JsonElement getParameter(JsonObject jsonObject, String param) throws WSMessageException;

	JsonObject getJsonObjectParameter(JsonObject jsonObject, String param) throws WSMessageException;

	String getStringParameter(JsonObject jsonObject, String param) throws WSMessageException;

	long getLongParameter(JsonObject jsonObject, String param) throws WSMessageException;

	boolean getBooleanParameter(JsonObject jsonObject, String param) throws WSMessageException;
}
