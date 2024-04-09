package com.govtech.payload;

import lombok.Data;

@Data
public class LocationProposeRequest {

	public Long id;
	private String proposedLocation;
	private String proposeBy;
	
}
