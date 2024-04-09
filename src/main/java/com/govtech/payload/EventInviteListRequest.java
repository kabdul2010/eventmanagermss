package com.govtech.payload;

import java.util.List;

import lombok.Data;

@Data
public class EventInviteListRequest {

	private Long id;
	private List<EventInviteDto> eventInviteList;
}
