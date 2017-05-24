package cz.ctu.ctuconference.utils.communication.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.ctu.ctuconference.contact.domain.ContactState;

import java.lang.reflect.Type;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class ContactStateTypeSerializer implements
		JsonSerializer<ContactState> {

	@Override
	public JsonElement serialize(ContactState contactState, Type type, JsonSerializationContext jsonSerializationContext) {
		Gson gson = new Gson();
		return gson.toJsonTree(contactState.getValue());
	}

}
