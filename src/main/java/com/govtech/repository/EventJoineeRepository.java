package com.govtech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.govtech.entity.EventInvite;
import com.govtech.entity.EventJoinee;


public interface EventJoineeRepository extends JpaRepository<EventJoinee, Long> {
	
	@Query(value = " select * from event_joinee where event_id=:eventId", nativeQuery = true)
	List<EventJoinee> findAllJoineedByEventId(@Param("eventId") Long eventId);
	
}
