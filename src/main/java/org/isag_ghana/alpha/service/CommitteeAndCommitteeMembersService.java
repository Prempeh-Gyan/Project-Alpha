package org.isag_ghana.alpha.service;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommitteeAndCommitteeMembersService {

	public CommitteeAndCommitteeMember findById(Long id);

	public void save(CommitteeAndCommitteeMember committeeAndCommitteeMember);

	public void save(List<CommitteeAndCommitteeMember> committeeAndCommitteeMember);

	public List<CommitteeAndCommitteeMember> findAllCommitteesByUser(User user);

	public List<CommitteeAndCommitteeMember> findAllCurrentCommitteesByUser(User user);

	public List<CommitteeAndCommitteeMember> findCommitteeByUserAndCommittee(User user, Committee committee);

	public List<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(Committee committee);

	public Page<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(Committee committee,
			Pageable pageable);

	public Page<CommitteeAndCommitteeMember> findAllCurrentCommitteesAndMembers(Pageable pageable);

	public Page<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests(Pageable pageable);

	public List<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests();

	void delete(CommitteeAndCommitteeMember entity);

	public Page<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests(Pageable pageable);

	public List<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests();

}