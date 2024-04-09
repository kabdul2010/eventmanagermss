package com.govtech.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.govtech.entity.EventSetup;

import jakarta.transaction.Transactional;

public interface EventSetupRepository extends JpaRepository<EventSetup, Long> {
	
	
	@Query(value = "SELECT es.*  FROM event_setup es,event_invite ei WHERE  ei.event_id=es.id and ei.user= :user and es.session_status=:sessionStatus", nativeQuery = true)
	Collection<EventSetup> findEventsByUsersss(String user,String sessionStatus);
	
	
	
	@Query(value = "SELECT *  FROM event_setup  WHERE session_status = :sessionStatus", nativeQuery = true)
	Collection<EventSetup> findEventsBySessionStatus(String sessionStatus);
	
	
	@Query(value = "SELECT *  FROM event_setup  WHERE organized_by = :organizedBy", nativeQuery = true)
	Collection<EventSetup>  findAllActiveUsersNative(String organizedBy);
	
	
	@Query(value = "SELECT *  FROM event_setup  WHERE organized_by = :organizedBy and session_status = :sessionStatus", nativeQuery = true)
	Collection<EventSetup>  findAllActiveUsersNative(String organizedBy,String sessionStatus);
	
	
	@Query(value = "SELECT *  FROM event_setup  WHERE id = :id", nativeQuery = true)
	EventSetup findRecordById(Long id);
	
	
	
	@Query(value = "select id as eventId,session_status as sessionStatus,date_created as dateCreated ,name as eventname from event_setup WHERE organized_by = :organizedBy", nativeQuery = true)
	Collection<EventSetup> findByorganizedBy(String organizedBy);
	
	
	@Query("select p from EventSetup p where p.organizedBy = :organizedBy")
	Collection<EventSetup> findByForenameAndSurname(@Param("organizedBy") String organizedBy);
	
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE EventSetup st SET st.sessionStatus=:sessionStatus WHERE st.eventId = :eventId")
	public int updateEnd(@Param("sessionStatus")String sessionStatus, @Param("eventId")Long eventId);
	
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE EventSetup st SET st.sessionStatus=:sessionStatus,st.randomSelectedPlace=:randomSelectedPlace WHERE st.eventId = :eventId")
	public int updateEvent(@Param("sessionStatus")String sessionStatus,@Param("randomSelectedPlace")String randomSelectedPlace, @Param("eventId")Long eventId);

	


}
	
	

	
	 


