
package org.isag_ghana.alpha.repository;

import java.util.List;

import org.isag_ghana.alpha.model.Committee;
import org.isag_ghana.alpha.model.CommitteeAndCommitteeMember;
import org.isag_ghana.alpha.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitteeAndMembersRepository extends JpaRepository<CommitteeAndCommitteeMember, Long> {

	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.committeeMember = :user ORDER BY cm.memberSince DESC")
	public List<CommitteeAndCommitteeMember> findAllCommitteesByUser(@Param("user") User user);

	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.committeeMember = :user AND cm.isStillAMember = true AND cm.requestedToJoin = false ORDER BY cm.memberSince DESC")
	public List<CommitteeAndCommitteeMember> findAllCurrentCommitteesByUser(@Param("user") User user);

	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.committee = :committee AND cm.isStillAMember = true")
	public List<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(
			@Param("committee") Committee committee);

	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.committee = :committee AND cm.isStillAMember = true")
	public Page<CommitteeAndCommitteeMember> findCurrentCommitteeMembersByCommittee(
			@Param("committee") Committee committee, Pageable pageable);

	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.committeeMember = :user AND cm.isStillAMember = true AND cm.committee = :committee")
	public List<CommitteeAndCommitteeMember> findCommitteeByUserAndCommittee(@Param("user") User user,
			@Param("committee") Committee committee);
	
	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.isExecutiveCommittee = false AND cm.isStillAMember = true OR cm.requestedToLeave = true")
	public Page<CommitteeAndCommitteeMember> findAllCurrentCommitteesAndMembers(Pageable pageable);
	
	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.requestedToJoin = true ORDER BY cm.committee.name ASC")
	public Page<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests(Pageable pageable);
	
	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.requestedToJoin = true ORDER BY cm.committee.name ASC")
	public List<CommitteeAndCommitteeMember> findAllJoinCommitteeRequests();
	
	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.requestedToLeave = true ORDER BY cm.committee.name ASC")
	public Page<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests(Pageable pageable);
	
	@Query("SELECT cm FROM CommitteeAndCommitteeMember cm WHERE cm.requestedToLeave = true ORDER BY cm.committee.name ASC")
	public List<CommitteeAndCommitteeMember> findAllLeaveCommitteeRequests();

}
