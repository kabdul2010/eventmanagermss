package com.govtech.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.govtech.entity.EventProposal;
import com.govtech.entity.EventProposedLocation;

public interface EventProposedLocationRepository extends JpaRepository<EventProposedLocation, Long> {

	@Query(value = " select pl.date_created as proposedOn,pl.email as email, ep.name as placeName,us.user_name as proposedBy from event_proposed_location pl,user us ,event_place ep where ep.id=pl.place_id and pl.user_id=us.user_id", nativeQuery = true)
	Collection<EventProposal> findAllProposals();
	
	
	@Query(value = " select pl.date_created as proposedOn,pl.email as email, ep.name as placeName,us.user_name as proposedBy from event_proposed_location pl,user us ,event_place ep where ep.id=pl.place_id and pl.user_id=us.user_id and pl.event_id=:eventId", nativeQuery = true)
	List<EventProposal> findAllProposalsByEventId(@Param("eventId") Long eventId);
	
	@Query(value = " select pl.date_created as proposedOn,pl.email as email, ep.name as placeName,us.user_name as proposedBy from event_proposed_location pl,user us ,event_place ep where ep.id=pl.place_id and pl.user_id=us.user_id and pl.event_id=:eventId and pl.email=:email", nativeQuery = true)
	List<EventProposal> findUserProposalsByEventId(@Param("eventId") Long eventId,@Param("email") String email);

}
