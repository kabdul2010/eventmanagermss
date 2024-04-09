package com.govtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govtech.entity.UsersEntity;


public interface UsersEntityRepository extends JpaRepository<UsersEntity, Long>{
	UsersEntity findByUsername(String username);
}
