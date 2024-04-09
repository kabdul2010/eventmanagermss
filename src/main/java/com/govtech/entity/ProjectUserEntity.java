package com.govtech.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUserEntity {
	
	@Id
	@GeneratedValue(generator = "custom-id")
	@GenericGenerator(name = "custom-id", strategy = "com.govtech.util.CustomIdGenerator")
	@Column(name = "user_id")
	private String userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private ProjectEntity project;

	@Column(name = "user")
	private String user;

	@Column(name = "department")
	private String department;
	
	
}