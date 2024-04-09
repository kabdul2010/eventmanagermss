package com.govtech.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.entity.EventPlace;


public interface EventPlaceRepository extends JpaRepository<EventPlace, Long> {
	
    Optional<EventPlace> findByPlaceName(String plaeName);
	
	
}
