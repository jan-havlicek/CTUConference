package cz.ctu.ctuconference.utils.communication;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

/**
 * Created by Nick Nemame on 04.10.2016.
 */
@Component
public class SocketMessageDataParser implements ISocketMessageDataParser {

	@Override
	public JsonElement getParameter(JsonObject jsonObject, String param) throws WSMessageException {
		JsonElement element = jsonObject.get(param);
		if(element == null) {
			throw new WSMessageException("Missing required parameter '" + param + "'");
		}
		return element;
	}

	@Override
	public JsonObject getJsonObjectParameter(JsonObject jsonObject, String param) throws WSMessageException {
		try {
			JsonObject paramValue = this.getParameter(jsonObject, param).getAsJsonObject();
			return paramValue;
		} catch(WSMessageException e) {
			throw e;
		} catch(Exception e) {
			throw new WSMessageException("Parameter '"+param+"' is not recognised as JSON Object");
		}
	}

	@Override
	public String getStringParameter(JsonObject jsonObject, String param) throws WSMessageException {
		try {
			String paramValue = this.getParameter(jsonObject, param).getAsString();
			return paramValue;
		} catch(WSMessageException e) {
			throw e;
		} catch(Exception e) {
			throw new WSMessageException("Parameter '"+param+"' is not recognised as string");
		}
	}

	@Override
	public boolean getBooleanParameter(JsonObject jsonObject, String param) throws WSMessageException {
		try {
			boolean paramValue = this.getParameter(jsonObject, param).getAsBoolean();
			return paramValue;
		} catch(WSMessageException e) {
			throw e;
		} catch(Exception e) {
			throw new WSMessageException("Parameter '"+param+"' is not recognised as string");
		}
	}

	@Override
	public long getLongParameter(JsonObject jsonObject, String param) throws WSMessageException {
		try {
			long paramValue = this.getParameter(jsonObject, param).getAsLong();
			return paramValue;
		} catch(WSMessageException e) {
			throw e;
		} catch(Exception e) {
			throw new WSMessageException("Parameter '"+param+"' is not recognised as long type");
		}
	}
}
