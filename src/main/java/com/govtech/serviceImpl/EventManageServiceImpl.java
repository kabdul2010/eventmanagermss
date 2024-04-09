package com.govtech.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.govtech.entity.EventDetailSummaryResponse;
import com.govtech.entity.EventInvite;
import com.govtech.entity.EventInviteSummary;
import com.govtech.entity.EventJoinee;
import com.govtech.entity.EventPlace;
import com.govtech.entity.EventProposal;
import com.govtech.entity.EventProposalSummary;
import com.govtech.entity.EventProposedLocation;
import com.govtech.entity.EventSetup;
import com.govtech.entity.User;
import com.govtech.payload.EventInviteDto;
import com.govtech.payload.EventInviteListRequest;
import com.govtech.payload.EventPlaceRequest;
import com.govtech.payload.JoinEventRequest;
import com.govtech.payload.LocationProposeRequest;
import com.govtech.repository.EventInviteRepository;
import com.govtech.repository.EventJoineeRepository;
import com.govtech.repository.EventPlaceRepository;
import com.govtech.repository.EventProposedLocationRepository;
import com.govtech.repository.EventSetupRepository;
import com.govtech.repository.UserRepository;
import com.govtech.service.EventManageService;

@Service
public class EventManageServiceImpl implements EventManageService {

	public static final String INVITE_REQ_SENT = "Invited for Session";
	public static final String EVENT_JOINED = "Joined Session";

	@Autowired
	private EventPlaceRepository eventPlaceRepository;

	@Autowired
	private EventSetupRepository eventSetupRepository;

	@Autowired
	private EventInviteRepository eventInviteRepository;

	@Autowired
	private EventJoineeRepository eventJoineeRepository;

	@Autowired
	private EventProposedLocationRepository eventProposedLocationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<EventSetup> findAllEventsByUser(String userMail) {
		Collection<Object[]> records = eventSetupRepository.findAllActiveUsersNative(userMail);
		System.out.println("records:::" + records);

		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventname((String) obj[2]);
			e1.setDescription((String) obj[2]);
			list.add(e1);

		}
		return list;
	}

	@Override
	public List<EventSetup> findAllDetailEventsByUser(String userMail) {
		Collection<Object[]> records = eventSetupRepository.findAllActiveUsersNative(userMail);
		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventId((Long) obj[0]);
			e1.setDateCreated((Date) obj[1]);
			e1.setEventname((String) obj[2]);
			e1.setOrganizedBy((String) obj[3]);
			e1.setDescription((String) obj[5]);
			list.add(e1);

		}
		return list;
	}

	@Override
	public List<EventSetup> myActiveSessionsByUsers(String userMail) {
		Collection<Object[]> records = eventSetupRepository.findEventsByUsersss(userMail, "OPEN");

		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventId((Long) obj[0]);
			e1.setDateCreated((Date) obj[1]);
			e1.setEventname((String) obj[2]);
			e1.setOrganizedBy((String) obj[3]);
			e1.setDescription((String) obj[5]);
			list.add(e1);

		}
		return list;
	}

	@Override
	public List<EventSetup> findEventsBySessionStatus() {
		Collection<Object[]> records = eventSetupRepository.findEventsBySessionStatus("OPEN");

		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventId((Long) obj[0]);
			e1.setDateCreated((Date) obj[1]);
			e1.setEventname((String) obj[2]);
			e1.setOrganizedBy((String) obj[3]);
			e1.setDescription((String) obj[5]);
			list.add(e1);

		}
		return list;
	}

	@Override
	public List<EventSetup> findEventsBySessionStatusByUsers(String userEmail) {
		Collection<Object[]> records = eventSetupRepository.findAllActiveUsersNative(userEmail, "OPEN");

		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventId((Long) obj[0]);
			e1.setDateCreated((Date) obj[1]);
			e1.setEventname((String) obj[2]);
			e1.setOrganizedBy((String) obj[3]);

			e1.setDescription((String) obj[5]);
			list.add(e1);

		}
		return list;
	}

	@Override
	public List<EventSetup> findEndEventsBySessionStatusByUsers(String userEmail) {
		Collection<Object[]> records = eventSetupRepository.findEventsBySessionStatus("END");
		System.out.println("records:::" + records);

		List<EventSetup> list = new ArrayList<>();
		EventSetup e1 = null;

		for (Object[] obj : records) {
			e1 = new EventSetup();
			e1.setEventId((Long) obj[0]);
			e1.setDateCreated((Date) obj[1]);
			e1.setEventname((String) obj[2]);
			e1.setOrganizedBy((String) obj[3]);
			e1.setDescription((String) obj[5]);
			e1.setRandomSelectedPlace((String) obj[7]);

			list.add(e1);

		}
		return list;
	}

	@Override
	public Collection<EventPlace> findAllPlaces() {
		Collection<EventPlace> list = eventPlaceRepository.findAll();
		return list;
	}

	@Override
	public EventSetup getEventSessionRecord(Long eventId) {
		Object eventSetupRecord = eventSetupRepository.findRecordById(eventId);
		Object obj[] = (Object[]) eventSetupRecord;
		EventSetup e1 = new EventSetup();
		e1.setEventId((Long) obj[0]);
		e1.setDateCreated((Date) obj[1]);
		e1.setEventname((String) obj[2]);
		e1.setOrganizedBy((String) obj[3]);
		e1.setDescription((String) obj[5]);
		return e1;
	}

	@Override
	public EventDetailSummaryResponse getEventDetailSummaryRecord(Long eventid) {
		Object eventSetupRecord = eventSetupRepository.findRecordById(eventid);
		Object obj[] = (Object[]) eventSetupRecord;
		EventSetup e1 = new EventSetup();
		e1.setEventId((Long) obj[0]);
		e1.setDateCreated((Date) obj[1]);
		e1.setEventname((String) obj[2]);
		e1.setOrganizedBy((String) obj[3]);
		e1.setDescription((String) obj[5]);
		e1.setRandomSelectedPlace((String) obj[7]);
		EventDetailSummaryResponse eventDetailSummaryResponse = modelMapper.map(e1, EventDetailSummaryResponse.class);
		Collection<EventProposal> records = eventProposedLocationRepository.findAllProposalsByEventId(eventid);
		List<EventProposalSummary> proposals = new ArrayList<>();
		EventProposalSummary propSummary = null;
		for (EventProposal prop : records) {
			propSummary = modelMapper.map(prop, EventProposalSummary.class);
			proposals.add(propSummary);
		}
		eventDetailSummaryResponse.setProposals(proposals);
		return eventDetailSummaryResponse;
	}

	@Override
	public EventDetailSummaryResponse myEventDetailRecord(Long eventid) {

		Object eventSetupRecord = eventSetupRepository.findRecordById(eventid);

		Object obj[] = (Object[]) eventSetupRecord;
		EventSetup e1 = new EventSetup();

		e1.setEventId((Long) obj[0]);
		e1.setDateCreated((Date) obj[1]);
		e1.setEventname((String) obj[2]);
		e1.setOrganizedBy((String) obj[3]);
		e1.setDescription((String) obj[5]);
		e1.setRandomSelectedPlace((String) obj[7]);

		EventDetailSummaryResponse eventDetailSummaryResponse = modelMapper.map(e1, EventDetailSummaryResponse.class);
		Collection<EventInvite> records = eventInviteRepository.findAllInviteesByEventId(eventid);

		List<EventInviteSummary> proposals = new ArrayList<>();
		EventInviteSummary propSummary = null;
		for (EventInvite prop : records) {
			propSummary = new EventInviteSummary();
			propSummary.setInviteStatus(prop.getInviteStatus());
			propSummary.setUser(prop.getUser());
			proposals.add(propSummary);
		}

		eventDetailSummaryResponse.setInvitees(proposals);
		return eventDetailSummaryResponse;
	}

	@Override
	public EventProposedLocation createProposeLocation(EventPlaceRequest eventSetupRequest, String userEmail) {
		EventProposedLocation eventProposedLocation = new EventProposedLocation();
		Collection<EventProposal> records = eventProposedLocationRepository
				.findUserProposalsByEventId(eventSetupRequest.getEventId(), userEmail);
		if (records.isEmpty()) {
			Optional<EventPlace> eventPlace = eventPlaceRepository.findByPlaceName(eventSetupRequest.getPlaceName());

			eventProposedLocation.setDateCreated(new Date());
			EventSetup eventSetup = new EventSetup();
			eventSetup.setEventId(eventSetupRequest.getEventId());
			eventProposedLocation.setEventSetup(eventSetup);
			eventProposedLocation.setEventPlace(eventPlace.get());
			eventProposedLocation.setEmail(userEmail);
			Optional<User> user = userRepository.findByEmail(userEmail);
			eventProposedLocation.setUser(user.get());

			eventProposedLocationRepository.save(eventProposedLocation);
		}
		return eventProposedLocation;
	}

	@Override
	public EventProposedLocation createPlaceProposeLocation(EventPlaceRequest eventSetupRequest, String userEmail) {
		EventPlace eventPlace = modelMapper.map(eventSetupRequest, EventPlace.class);
		eventPlace.setCreatedBy(userEmail);
		Optional<EventPlace> eventPlaceOp = eventPlaceRepository.findByPlaceName(eventSetupRequest.getPlaceName());
		if (!eventPlaceOp.isPresent()) {
			eventPlace = eventPlaceRepository.save(eventPlace);
		}

		EventProposedLocation eventProposedLocation = new EventProposedLocation();
		eventProposedLocation.setDateCreated(new Date());
		EventSetup eventSetup = new EventSetup();
		eventSetup.setEventId(eventSetupRequest.getEventId());
		eventProposedLocation.setEventSetup(eventSetup);
		eventProposedLocation.setEventPlace(eventPlace);
		eventProposedLocation.setEmail(userEmail);
		Optional<User> user = userRepository.findByUsername(userEmail);
		eventProposedLocation.setUser(user.get());

		eventProposedLocationRepository.save(eventProposedLocation);
		return eventProposedLocation;
	}

	@Override
	public EventInviteSummary endEventSubmit(Long eventid) {
		EventInviteSummary propsal = new EventInviteSummary();

		List<EventProposal> records = eventProposedLocationRepository.findAllProposalsByEventId(eventid);
		if (!records.isEmpty()) {
			EventProposal selected = randomPlaceSelection(records);
			eventSetupRepository.updateEvent("END", selected.getPlaceName(), eventid);
		} else {
			eventSetupRepository.updateEvent("END", "", eventid);
		}

		return propsal;
	}

	@Override
	public EventSetup joinEvent(Long eventid, String userEmail) {

		eventInviteRepository.updateEventInviteStauts("Joined Session", eventid, userEmail);
		EventSetup eventSetup = new EventSetup();

		return eventSetup;
	}

	public EventProposal randomPlaceSelection(List<EventProposal> proposals) {
		Random random = new Random();
		return proposals.get(random.nextInt(proposals.size()));
	}

	@Override
	public String proposeEventLocation(LocationProposeRequest locationProposeRequest) {
		EventSetup eventSetup = modelMapper.map(locationProposeRequest, EventSetup.class);
		EventProposedLocation eventProposedLocation = new EventProposedLocation();
		eventProposedLocation.setDateCreated(new Date());
		eventProposedLocation.setEventSetup(eventSetup);
		eventProposedLocation.setEmail(locationProposeRequest.getProposeBy());
		System.out.println("fff" + locationProposeRequest.getProposedLocation() + "<<<<");
		// Optional<EventPlace> eventPlace =
		// eventPlaceRepository.findByName(locationProposeRequest.getProposedLocation());
		// System.out.println("eventPlace>>>>>>>" + eventPlace.isPresent());
		// eventProposedLocation.setEventPlace(eventPlace.get());
		Optional<User> user = userRepository.findByUsername(locationProposeRequest.getProposeBy());

		System.out.println("user>>>>>>>" + user.isPresent());
		eventProposedLocation.setUser(user.get());
		System.out.println(eventProposedLocation.getEventSetup() + "--->" + eventProposedLocation.getEmail() + "--->"
				+ eventProposedLocation.getEventPlace());
		eventProposedLocationRepository.save(eventProposedLocation);
		return null;
	}

	@Override
	public EventSetup createSession(EventSetup eventSetup) {
		eventSetup.setSessionStatus("OPEN");
		eventSetup.setDateCreated(new Date());
		/*
		 * System.out.println("eventSetup:::" + eventSetup.getSessionUsers());
		 * 
		 * eventSetup.getSessionUsers().forEach(inviteReq -> {
		 * 
		 * EventInvite eventInvite = modelMapper.map(inviteReq, EventInvite.class);
		 * eventInvite.setEventSetup(eventSetup);
		 * eventInvite.setInviteStatus(INVITE_REQ_SENT); });
		 */
		return eventSetupRepository.save(eventSetup);
	}

	@Override
	public EventSetup createSession(EventSetup eventSetup, List<EventInviteDto> inviteList) {
		eventSetup.setSessionStatus("OPEN");
		eventSetup.setDateCreated(new Date());
		System.out.println("eventSetup:::" + inviteList);
		eventSetup = eventSetupRepository.save(eventSetup);
		for (EventInviteDto dto : inviteList) {

			EventInvite eventInvite = modelMapper.map(dto, EventInvite.class);
			eventInvite.setEventSetup(eventSetup);
			eventInvite.setInviteStatus(INVITE_REQ_SENT);
			eventInviteRepository.save(eventInvite);
		}
		return eventSetup;
	}

	@Override
	public EventSetup setupEvent(EventSetup eventSetup) {
		return eventSetupRepository.save(eventSetup);
	}

	@Override
	public void inviteTeam(EventInviteListRequest eventSetupRequest) {
		EventSetup eventSetup = modelMapper.map(eventSetupRequest, EventSetup.class);
		eventSetupRequest.getEventInviteList().forEach(inviteReq -> {
			EventInvite eventInvite = modelMapper.map(inviteReq, EventInvite.class);
			eventInvite.setEventSetup(eventSetup);
			eventInvite.setInviteStatus(INVITE_REQ_SENT);
			eventInviteRepository.save(eventInvite);
		});

	}

	@Override
	public String joinEvent(JoinEventRequest joinEventRequest) {
		EventSetup eventSetup = modelMapper.map(joinEventRequest, EventSetup.class);
		EventJoinee eventJoinee = modelMapper.map(joinEventRequest, EventJoinee.class);
		eventJoinee.setEventSetup(eventSetup);
		System.out.println(eventJoinee.getEmail() + "--->" + eventJoinee.getEventSetup().getEventId());
		eventJoinee.setJoineeStatus(EVENT_JOINED);
		eventJoineeRepository.save(eventJoinee);
		return null;
	}

	@Override
	public EventPlace createEventPlace(EventPlace eventPlace) {
		return eventPlaceRepository.save(eventPlace);
	}

	@Override
	public EventSetup endEvent(EventSetup eventSetup) {
		return eventSetupRepository.save(eventSetup);
	}

	@Override
	public EventSetup eventSummary(EventSetup eventSetup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventSetup sendInvite(EventSetup eventSetup) {
		// TODO Auto-generated method stub
		return null;
	}

}
