package com.govtech.payload;

import lombok.Data;

@Data
public class EventPlaceRequest {

	private Long eventId;
	
	private Long id;
	
	private String placeName;
	
	private String location;
	
}
