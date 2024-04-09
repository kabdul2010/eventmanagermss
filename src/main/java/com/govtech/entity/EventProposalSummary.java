package com.govtech.entity;

import java.util.Date;

import lombok.Data;


@Data
public class EventProposalSummary {
	 private Long id;
	 private Long eventId;
	 private String placeName;
	 private String proposedBy;
	 private Date proposedOn;
	 
	 
	

}
