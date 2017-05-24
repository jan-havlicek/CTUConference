package cz.ctu.ctuconference.utils.communication.json;

import com.google.gson.*;
import cz.ctu.ctuconference.contact.domain.ContactAction;

import java.lang.reflect.Type;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class ContactActionTypeSerializer implements
		JsonSerializer<ContactAction> {

	@Override
	public JsonElement serialize(ContactAction contactAction, Type type, JsonSerializationContext jsonSerializationContext) {
		Gson gson = new Gson();
		return gson.toJsonTree(contactAction.getValue());
	}

}
