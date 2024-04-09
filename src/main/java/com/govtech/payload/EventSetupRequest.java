package com.govtech.payload;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventSetupRequest {

	@NotBlank
	private String eventName;

	
	private String organized;
	
	

	private String description;
	
	private List<EventInviteDto> sessionUsers;

}
