package com.govtech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.govtech.entity.JobAssigneeEntity;


@Repository
public interface JobAssigneeRepository extends JpaRepository<JobAssigneeEntity,Long> {
}
