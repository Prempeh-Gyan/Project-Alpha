package org.isag_ghana.alpha.serviceImpl;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.CommitteeAndMembersRepository;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommitteeAndCommitteeMembersServiceImpl implements CommitteeAndCommitteeMembersService {

	private final CommitteeAndMembersRepository committeeAndMembersRepository;

	@Autowired
	public CommitteeAndCommitteeMembersServiceImpl(CommitteeAndMembersRepository committeeAndMembersRepository) {
		this.committeeAndMembersRepository = committeeAndMembersRepository;
	}

	@Override
	public CommitteeAndCommitteeMember findById(Long id) {
		return committeeAndMembersRepository.findOne(id);
	}

	@Override
	public void save(CommitteeAndCommitteeMember committeeAndCommitteeMember) {
		committeeAndMembersRepository.save(committeeAndCommitteeMember);
	}

	@Override
	public void save(List<CommitteeAndCommitteeMember> committeeAndCommitteeMember) {
		committeeAndMembersRepository.save(committeeAndCommitteeMember);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findAllCommitteesByUser(User user) {
		return committeeAndMembersRepository.findAllCommitteesByUser(user);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findAllCurrentCommitteesByUser(User user) {
		return committeeAndMembersRepository.findAllCurrentCommitteesByUser(user);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(Committee committee) {
		return committeeAndMembersRepository.findCurrentCommitteeMembersByCommittee(committee);
	}

	@Override
	public Page<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(Committee committee,
			Pageable pageable) {
		return committeeAndMembersRepository.findCurrentCommitteeMembersByCommittee(committee, pageable);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findCommitteeByUserAndCommittee(User user, Committee committee) {
		return committeeAndMembersRepository.findCommitteeByUserAndCommittee(user, committee);
	}

	@Override
	public Page<CommitteeAndCommitteeMember> findAllCurrentCommitteesAndMembers(Pageable pageable) {
		return committeeAndMembersRepository.findAllCurrentCommitteesAndMembers(pageable);
	}

	@Override
	public Page<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests(Pageable pageable) {
		return committeeAndMembersRepository.findAllJoinCommitteeRequests(pageable);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests() {
		return committeeAndMembersRepository.findAllJoinCommitteeRequests();
	}

	@Override
	public void delete(CommitteeAndCommitteeMember entity) {
		committeeAndMembersRepository.delete(entity);
	}

	@Override
	public Page<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests(Pageable pageable) {
		return committeeAndMembersRepository.findAllLeaveCommitteeRequests(pageable);
	}

	@Override
	public List<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests() {
		return committeeAndMembersRepository.findAllLeaveCommitteeRequests();
	}

}
