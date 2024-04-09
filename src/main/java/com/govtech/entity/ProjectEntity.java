package com.govtech.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@Table(name = "project")
@AllArgsConstructor
@NoArgsConstructor

public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "project_cost")
    private double projectCost;

    @Column(name = "project_head")
    private String projectHead;

    @Column(name = "rate_per_hour")
    private double rph;

    @Column(name = "project_manager")
    private String projectManager;

    @JsonProperty("projectUsers")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private List<ProjectUserEntity> users;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private List<ProjectUserEntity> departments;

    @Column(name = "description")
    private String description;

}