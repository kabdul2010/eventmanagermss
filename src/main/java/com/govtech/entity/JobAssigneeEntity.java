package com.govtech.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_assignees")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobAssigneeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignee_id")
    private Long assigneeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobEntity jobEntity;

    @OneToMany(mappedBy = "jobAssigneeEntity", cascade = CascadeType.ALL)
    private List<UsersEntity> users;

    @Column(name = "department")
    private String department;

}
