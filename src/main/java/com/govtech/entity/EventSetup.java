package com.govtech.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "event_setup")
public class EventSetup {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventId;

	@NotBlank
	@Size(max = 20)
	@Column(name = "name")
	private String eventname;

	@NotBlank
	@Size(max = 100)
	@Column(name = "organized_by")
	private String organizedBy;

	@Size(max = 2000)
	@Column(name = "description")
	private String description;

	@Column(length = 50, name = "session_status")
	private String sessionStatus;

	@Column(name = "date_created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Column(name = "date_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEnd;

	@Size(max = 200)
	@Column(name = "selected_place")
	private String randomSelectedPlace;


}