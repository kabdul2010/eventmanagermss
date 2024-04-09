package com.govtech.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EventDetailSummaryResponse {
	private Long eventId;
	private String eventname;
	private String organizedBy;
	private Date dateCreated;
	private String description;
	private List<EventProposalSummary> proposals;
	
	private List<EventInviteSummary> invitees;
	
	
	
	private String randomSelectedPlace;
	
	

}
