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

import com.govtech.dtos.EmailDetail;
import com.govtech.entity.EventDetailSummaryResponse;
import com.govtech.entity.EventInvite;
import com.govtech.entity.EventInviteSummary;
import com.govtech.entity.EventPlace;
import com.govtech.entity.EventProposal;
import com.govtech.entity.EventProposalSummary;
import com.govtech.entity.EventProposedLocation;
import com.govtech.entity.EventSetup;
import com.govtech.entity.User;
import com.govtech.payload.EventInviteDto;
import com.govtech.payload.EventInviteListRequest;
import com.govtech.payload.EventPlaceRequest;
import com.govtech.payload.LocationProposeRequest;
import com.govtech.repository.EventInviteRepository;
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
	private EventProposedLocationRepository eventProposedLocationRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;
	
	
	
	@Autowired
	private EmailTriggerService emailTriggerService;
	

	@Override
	public Collection<EventSetup> findAllEventsByUser(String userMail) {
		return eventSetupRepository.findAllActiveUsersNative(userMail);
		
	
	}

	@Override
	public Collection<EventSetup> findAllDetailEventsByUser(String userMail) {
		return eventSetupRepository.findAllActiveUsersNative(userMail);
	
	}

	@Override
	public Collection<EventSetup> myActiveSessionsByUsers(String userMail) {
		Collection<EventSetup> records = eventSetupRepository.findEventsByUsersss(userMail, "OPEN");

	
		return records;
	}

	@Override
	public Collection<EventSetup> findEventsBySessionStatus() {
		return  eventSetupRepository.findEventsBySessionStatus("OPEN");


	}

	@Override
	public Collection<EventSetup> findEventsBySessionStatusByUsers(String userEmail) {
		return eventSetupRepository.findAllActiveUsersNative(userEmail, "OPEN");

	
	}

	@Override
	public Collection<EventSetup> findEndEventsBySessionStatusByUsers(String userEmail) {
		return eventSetupRepository.findEventsBySessionStatus("END");
		
	}

	@Override
	public Collection<EventPlace> findAllPlaces() {
		Collection<EventPlace> list = eventPlaceRepository.findAll();
		return list;
	}

	@Override
	public EventSetup getEventSessionRecord(Long eventId) {
		EventSetup e1= eventSetupRepository.findRecordById(eventId);
		return e1;
	}

	@Override
	public EventDetailSummaryResponse getEventDetailSummaryRecord(Long eventid) {
		EventSetup e1= eventSetupRepository.findRecordById(eventid);
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

		EventSetup e1= eventSetupRepository.findRecordById(eventid);


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
		Optional<User> user = userRepository.findByEmail(userEmail);
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
			
			
			List result=eventInviteRepository.findJoinedTeam(eventid);
			System.out.println("resultttttttttttttttttttt"+result);
			
			/*EmailDetail emailDetail=new EmailDetail();
			
			emailDetail.setContent("session ID"+eventid+"\nSession Ended and Random Selection "+selected.getPlaceName());
			emailDetail.setSubject("Session Ended and Random Selection "+selected.getPlaceName());
			emailDetail.setToAddress(eventInvite.getUser());
			
			
			inviteEmailTrigger(emailDetail);*/
			
			
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
		Optional<User> user = userRepository.findByUsername(locationProposeRequest.getProposeBy());

		eventProposedLocation.setUser(user.get());

		eventProposedLocationRepository.save(eventProposedLocation);
		return null;
	}

	@Override
	public EventSetup createSession(EventSetup eventSetup) {
		eventSetup.setSessionStatus("OPEN");
		eventSetup.setDateCreated(new Date());
		return eventSetupRepository.save(eventSetup);
	}

	@Override
	public EventSetup createSession(EventSetup eventSetup, List<EventInviteDto> inviteList) {
		eventSetup.setSessionStatus("OPEN");
		eventSetup.setDateCreated(new Date());

		eventSetup = eventSetupRepository.save(eventSetup);
		EmailDetail emailDetail=new EmailDetail();
		for (EventInviteDto dto : inviteList) {

			EventInvite eventInvite = modelMapper.map(dto, EventInvite.class);
			eventInvite.setEventSetup(eventSetup);
			eventInvite.setInviteStatus(INVITE_REQ_SENT);
			eventInviteRepository.save(eventInvite);
			emailDetail.setContent("Hi "+eventInvite.getUser()+"\n"+" "+eventSetup.getOrganizedBy()+" invited to Join session \n Pls click below to Join the Session \n http://localhost:4200/event-location-details/"+eventSetup.getEventId());
			emailDetail.setSubject("Invite for Session from "+eventSetup.getOrganizedBy());
			emailDetail.setToAddress(eventInvite.getUser());
			inviteEmailTrigger(emailDetail);
			
		}
		return eventSetup;
	}
    private void inviteEmailTrigger(EmailDetail emailDetail) {
    	emailTriggerService.emailTrigger(emailDetail);
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
