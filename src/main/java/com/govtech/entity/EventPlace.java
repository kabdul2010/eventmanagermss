package com.govtech.entity;

import org.checkerframework.common.aliasing.qual.Unique;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "event_place")
public class EventPlace {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Size(max = 100)
	@Unique
	@Column(name = "name")
	private String placeName;


	
	@NotBlank
	@Size(max = 50)
	@Column(name = "created_by")
	private String createdBy;
	
	
	
	
	@Size(max = 200)
	@Column(name = "address")
	private String location;

}