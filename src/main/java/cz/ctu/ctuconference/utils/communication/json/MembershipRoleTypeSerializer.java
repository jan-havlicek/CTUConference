package cz.ctu.ctuconference.utils.communication.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import cz.ctu.ctuconference.group.domain.MembershipRole;

import java.lang.reflect.Type;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public class MembershipRoleTypeSerializer implements
		JsonSerializer<MembershipRole> {

	@Override
	public JsonElement serialize(MembershipRole membershipRole, Type type, JsonSerializationContext jsonSerializationContext) {
		Gson gson = new Gson();
		return gson.toJsonTree(membershipRole.getValue());
	}

}
