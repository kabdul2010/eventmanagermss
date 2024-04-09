package com.govtech.service;

import java.util.Collection;
import java.util.List;

import com.govtech.entity.EventDetailSummaryResponse;
import com.govtech.entity.EventInviteSummary;
import com.govtech.entity.EventPlace;
import com.govtech.entity.EventProposedLocation;
import com.govtech.entity.EventSetup;
import com.govtech.payload.EventInviteDto;
import com.govtech.payload.EventInviteListRequest;
import com.govtech.payload.EventPlaceRequest;
import com.govtech.payload.JoinEventRequest;
import com.govtech.payload.LocationProposeRequest;

public interface EventManageService {

	public EventSetup setupEvent(EventSetup eventSetup);

	public EventSetup createSession(EventSetup eventSetup);

	public EventSetup createSession(EventSetup eventSetup, List<EventInviteDto> inviteList);

	public void inviteTeam(EventInviteListRequest eventSetup);

	public EventSetup eventSummary(EventSetup eventSetup);

	public EventSetup endEvent(EventSetup eventSetup);

	public EventSetup sendInvite(EventSetup eventSetup);

	public String joinEvent(JoinEventRequest joinEventRequest);

	public EventPlace createEventPlace(EventPlace eventPlace);

	public String proposeEventLocation(LocationProposeRequest locationProposeRequest);

	public List<EventSetup> findAllEventsByUser(String userMail);

	public List<EventSetup> findAllDetailEventsByUser(String userMail);

	public List<EventSetup> myActiveSessionsByUsers(String userMail);

	public List<EventSetup> findEventsBySessionStatus();

	public List<EventSetup> findEventsBySessionStatusByUsers(String userEmail);

	public List<EventSetup> findEndEventsBySessionStatusByUsers(String userEmail);

	public Collection<EventPlace> findAllPlaces();

	public EventSetup getEventSessionRecord(Long eventId);

	public EventDetailSummaryResponse getEventDetailSummaryRecord(Long eventid);

	public EventDetailSummaryResponse myEventDetailRecord(Long eventid);

	public EventProposedLocation createProposeLocation(EventPlaceRequest eventSetupRequest, String userEmail);

	public EventProposedLocation createPlaceProposeLocation(EventPlaceRequest eventSetupRequest, String userEmail);

	public EventInviteSummary endEventSubmit(Long eventid);

	public EventSetup joinEvent(Long eventid, String userEmail);

}
