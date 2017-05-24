package cz.ctu.ctuconference.utils.communication;

import cz.ctu.ctuconference.group.domain.Group;

/**
 * Created by Nick nemame on 24.12.2016.
 */
public interface GroupMessageModifier {
	void modifyGroupMessage(Group group, Object message);
}
