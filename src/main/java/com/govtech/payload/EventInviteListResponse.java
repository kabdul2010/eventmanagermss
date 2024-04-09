package com.govtech.payload;

import java.util.List;

import lombok.Data;

@Data
public class EventInviteListResponse {

	private Long id;
	private List<EventInviteDto> eventInviteList;
}



