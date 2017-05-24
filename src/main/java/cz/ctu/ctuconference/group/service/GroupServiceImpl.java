package cz.ctu.ctuconference.group.service;

import cz.ctu.ctuconference.contact.domain.ContactAuthState;
import cz.ctu.ctuconference.conversation.domain.GroupConversation;
import cz.ctu.ctuconference.group.domain.*;
import cz.ctu.ctuconference.group.dto.MembershipDTO;
import cz.ctu.ctuconference.group.service.dao.GroupDAO;
import cz.ctu.ctuconference.group.service.dao.GroupMembershipDAO;
import cz.ctu.ctuconference.user.AppUser;
import cz.ctu.ctuconference.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nick nemame on 20.11.2016.
 */
@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	UserDAO userDAO;

	@Autowired
	GroupDAO groupDAO;

	@Autowired
	GroupMembershipDAO groupMembershipDAO;

	@Override
	public List<AppUser> getGroupMembers(long groupId) {
		return getMembers(groupId, null);
	}

	@Override
	public List<AppUser> getAdminMembers(long groupId) {
		return getMembers(groupId, membership -> membership.getRole().hasPrivilege(MemberPrivilege.ADMINISTRATION));
	}

	@Override
	public List<AppUser> getLectors(long groupId) {
		return getMembers(groupId, membership -> membership.getRole().hasPrivilege(MemberPrivilege.ADMINISTRATION.CALL_PERFORMING));
	}

	interface MembershipConstraint { boolean check(GroupMembership membership); }

	private List<AppUser> getMembers(long groupId, MembershipConstraint constraint) {
		return groupDAO.getById(groupId).getMembershipList()
				.stream()
				.filter(membership -> membership.isAccepted()
						&& (constraint == null ? true : constraint.check(membership)))
				.map(membership -> membership.getUser())
				.collect(Collectors.toList());
	}


	@Override
	public List<MembershipDTO> getGroupMemberships(long groupId) {
		return groupDAO.getById(groupId).getMembershipList().stream()
				.map(membership -> new MembershipDTO(
					membership.getId(),
					membership.getUser().getId(),
					membership.getUser().getEmail(),
					membership.getUser().getFirstName(),
					membership.getUser().getLastName(),
					membership.getState(),
					membership.getRole()
				)).collect(Collectors.toList());
	}

	@Override
	public long createGroup(String name, GroupType type, long creatorId) {
		AppUser creator = userDAO.getById(creatorId);
		Group group = new Group(name, type);
		group.setDateCreated(new Date());
		GroupConversation conversation = new GroupConversation();
		conversation.setName("");
		group.setConversation(conversation);
		groupDAO.persist(group);
		GroupMembership membership = new GroupMembership(creator, group, ContactAuthState.ACCEPTED, MembershipRole.ADMIN, new Date());
		creator.getGroupMembershipList().add(membership);
		groupMembershipDAO.persist(membership);
		group.addMembership(membership);
		membership.setGroup(group);
		conversation.setGroup(group);
		groupDAO.flush();
		return group.getId();
	}

	@Override
	public void removeGroup(long groupId) {
		groupDAO.getById(groupId).setActive(false);
		groupDAO.flush();
	}

	@Override
	public GroupMembership requestMembership(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		boolean noMembershipAction = true;
		if(membership != null && membership.getState() == ContactAuthState.REMOVED) {
			membership.setState(ContactAuthState.WAITING);
			noMembershipAction = false;
		} else {
			if(membership == null) {
				AppUser user = userDAO.getById(userId);
				Group group = groupDAO.getById(groupId);
				membership = new GroupMembership(user, group, ContactAuthState.WAITING, null, new Date());
				group.addMembership(membership);
				user.addGroupMembership(membership);
				groupMembershipDAO.persist(membership);
				noMembershipAction = false;
			}
		}
		if(!noMembershipAction) groupMembershipDAO.flush();
		return membership;
	}

	@Override
	public GroupMembership acceptMembership(long userId, long groupId, MembershipRole role) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		membership.setState(ContactAuthState.ACCEPTED);
		membership.setRole(role);
		groupMembershipDAO.flush();
		return membership;
	}

	@Override
	public GroupMembership rejectMembership(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		membership.setState(ContactAuthState.REJECTED);
		groupMembershipDAO.flush();
		return membership;
	}

	@Override
	public GroupMembership removeMembership(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		membership.setState(ContactAuthState.REMOVED);
		groupMembershipDAO.flush();
		return membership;
	}

	@Override
	public GroupMembership changeRole(long userId, long groupId, MembershipRole role) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		membership.setRole(role);
		groupMembershipDAO.flush();
		return membership;
	}

	@Override
	public MembershipRole getMembershipRole(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		return membership.getRole();
	}

	@Override
	public boolean hasLectorPermissions(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		if(membership == null) return false;
		if((membership.getRole() == MembershipRole.ADMIN
				|| membership.getRole() == MembershipRole.LECTOR)
				&& membership.isAccepted()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasAdminPermissions(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		if(membership == null) return false;
		if(membership.getRole() == MembershipRole.ADMIN && membership.isAccepted()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isMember(long userId, long groupId) {
		GroupMembership membership = groupMembershipDAO.getMembership(userId, groupId);
		if(membership == null) return false;
		if(membership.isAccepted()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isActive(long groupId) {
		Group group = groupDAO.getById(groupId);
		return group.isActive();
	}
}
