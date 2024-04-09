package com.govtech.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.govtech.entity.EventInvite;
import com.govtech.entity.EventProposal;
import com.govtech.entity.EventSetup;

import jakarta.transaction.Transactional;


public interface EventInviteRepository extends JpaRepository<EventInvite, Long> {
	
	Collection<EventInvite> findByEventSetup(EventSetup eventSetup);
	
	@Query(value = " select * from event_invite where event_id=:eventId", nativeQuery = true)
	List<EventInvite> findAllInviteesByEventId(@Param("eventId") Long eventId);
	
	
	
	@Query(value = " select i.id,i.user,i.invite_status,i.event_id from event_invite i,event_setup s where s.id=i.event_id and i.event_id=:eventId", nativeQuery = true)
	List<EventInvite> findAllInviteByEventId(@Param("eventId") Long eventId);
	
	@Query(value = "select s.organized_by,i.user,i.event_id from event_invite i,event_setup s where s.id=i.event_id and i.event_id=:eventId and i.invite_status='Joined Session'", nativeQuery = true)
	List  findJoinedTeam(@Param("eventId") Long eventId);
	
	
	/*
	@Modifying
	@Transactional
	@Query(value = "UPDATE EventInvite st SET st.inviteStatus=:inviteStatus WHERE st.eventId = :eventId and st.user=:user")
	public int updateInviteStatus(@Param("inviteStatus")String inviteStatus,@Param("eventId")Long eventId,@Param("user")String user);
	*/
	
	
	@Query(value = "update event_invite set invite_status = :inviteStatus WHERE event_id = :eventId and user=:user", nativeQuery = true)
	@Modifying
	@Transactional
	void updateEventInviteStauts(@Param("inviteStatus")String inviteStatus,@Param("eventId")Long eventId,@Param("user")String user);

	
	
}
