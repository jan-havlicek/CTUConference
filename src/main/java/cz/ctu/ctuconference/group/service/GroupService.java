package cz.ctu.ctuconference.group.service;

import cz.ctu.ctuconference.group.domain.Group;
import cz.ctu.ctuconference.group.domain.GroupMembership;
import cz.ctu.ctuconference.group.domain.GroupType;
import cz.ctu.ctuconference.group.domain.MembershipRole;
import cz.ctu.ctuconference.group.dto.MembershipDTO;
import cz.ctu.ctuconference.user.AppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nick nemame on 20.11.2016.
 */
public interface GroupService {

	/* Group handlers */

	/**
	 * Author will be automatically in Administrator role.
	 * @param name
	 * @param type
	 * @param creatorId
	 */
	@Transactional
	long createGroup(String name, GroupType type, long creatorId);

	@Transactional
	void removeGroup(long groupId);


	/* Getters of members */

	@Transactional(readOnly = true)
	List<AppUser> getGroupMembers(long groupId);

	@Transactional(readOnly = true)
	List<AppUser> getAdminMembers(long groupId);

	@Transactional(readOnly = true)
	List<AppUser> getLectors(long groupId);

	@Transactional(readOnly = true)
	List<MembershipDTO> getGroupMemberships(long groupId);


	/* Membership handlers */

	@Transactional
	GroupMembership requestMembership(long userId, long groupId);

	@Transactional
	GroupMembership acceptMembership(long userId, long groupId, MembershipRole role);

	@Transactional
	GroupMembership rejectMembership(long userId, long groupId);

	@Transactional
	GroupMembership removeMembership(long userId, long groupId);

	@Transactional
	GroupMembership changeRole(long userId, long groupId, MembershipRole role);


	/* Permission getters */

	@Transactional(readOnly = true)
	MembershipRole getMembershipRole(long userId, long groupId);

	@Transactional(readOnly = true)
	boolean hasLectorPermissions(long userId, long groupId);

	@Transactional(readOnly = true)
	boolean hasAdminPermissions(long userId, long groupId);

	@Transactional(readOnly = true)
	boolean isMember(long userId, long groupId);

	@Transactional(readOnly = true)
	boolean isActive(long groupId);
}
