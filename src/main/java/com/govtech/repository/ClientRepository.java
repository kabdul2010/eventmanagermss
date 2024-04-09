package com.govtech.repository;

import com.govtech.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    public Optional<ClientEntity> findByMobile(String mobile);

    public Optional<ClientEntity> findByClientName(String clientName);

    
}
 
