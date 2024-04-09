package com.govtech.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.entity.EventDetailSummaryResponse;
import com.govtech.entity.EventInviteSummary;
import com.govtech.entity.EventPlace;
import com.govtech.entity.EventProposedLocation;
import com.govtech.entity.EventSetup;
import com.govtech.entity.RefreshToken;
import com.govtech.entity.RefreshTokenRequest;
import com.govtech.entity.User;
import com.govtech.entity.UserRequest;
import com.govtech.entity.UserResponse;
import com.govtech.exception.UserAlreadyExistsException;
import com.govtech.exception.UserNotFoundException;
import com.govtech.payload.EventInviteDto;
import com.govtech.payload.EventPlaceRequest;
import com.govtech.payload.EventSetupRequest;
import com.govtech.service.EventManageService;
import com.govtech.service.IUserService;
import com.govtech.serviceImpl.RefreshTokenService;
import com.govtech.util.JWTUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Slf4j
public class UserRestController {

	@Autowired
	private IUserService userService;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private UserDetailsService userDetailsService;



	// signup module endpoint

	@PostMapping("/saveUser")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		Optional<User> existingUserByUsername = userService.findByUsername(user.getUsername());
		if (existingUserByUsername.isPresent()) {
			throw new UserAlreadyExistsException("Provided Username '" + user.getUsername()
					+ "' Already Exists, Please Choose a Different Username");
		}
		// Check if email already exists
		Optional<User> existingUserByEmail = userService.findUserByEmail(user.getEmail());
		if (existingUserByEmail.isPresent()) {
			throw new UserAlreadyExistsException(
					"Provided Email '" + user.getEmail() + "' Already Exists, Please Choose a Different Email");
		}

		Integer id = userService.saveUser(user);
		String message = "User with id '" + id + "' saved succssfully!";
		return ResponseEntity.ok(message);

	}

	@PostMapping("/loginUser")
	public ResponseEntity<UserResponse> login(@RequestBody UserRequest request) {

		// Check if the username exists
		Optional<User> userOps = userService.findByUsername(request.getUsername());
		System.out.println("userOps:::::::::::" + userOps + "<<<<<<<<<<<");
		if (userOps.isEmpty()) {
			throw new UserNotFoundException(
					"Provided Username '" + request.getUsername() + "' does not exist, Please Signup First.");
		}

		this.authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

		RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userDetails.getUsername());

		// Extract roles from UserDetails
		Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());

		String token = jwtUtil.generateJWTToken(userDetails.getUsername(), roles);

		UserResponse response = UserResponse.builder().token(token).refreshToken(refreshToken.getRefreshToken())
				.message(userDetails.getUsername()).roles(roles) // Pass roles here instead of token
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	public UserResponse refreshJwtToken(@RequestBody RefreshTokenRequest request) {
		RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
		User user = refreshToken.getUser();
		String accessToken = jwtUtil.generateJWTToken(user.getUsername(), user.getRoles());
		return UserResponse.builder().refreshToken(refreshToken.getRefreshToken()).token(accessToken)
				.message(user.getUsername()).build();
	}

	@Autowired
	private EventManageService eventManageService;

	@Autowired
	private ModelMapper modelMapper;

	public String getUserEmailFromUserToken(String token) {
		String userToken = jwtUtil.getSubject(token);
		Optional<User> userOps = userService.findByUsername(userToken);
		return userOps.get().getEmail();
	}

	/**
	 * It is for create new session with users invited
	 * 
	 * @param headers
	 * @param eventSetupRequest
	 * @return
	 */
	@PostMapping("/eventsetup")
	public ResponseEntity<?> eventSetup(@RequestHeader HttpHeaders headers,
			@Valid @RequestBody EventSetupRequest eventSetupRequest) {
		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		EventSetup eventSetup = modelMapper.map(eventSetupRequest, EventSetup.class);
		eventSetup.setOrganizedBy(userEmail);
		List<EventInviteDto> inviteList = eventSetupRequest.getSessionUsers();
		eventSetup = eventManageService.createSession(eventSetup, inviteList);
		log.info("event created :" + eventSetup.getEventname() + " organized by :" + userEmail);
		Collection<EventSetup> list = eventManageService.findAllEventsByUser(userEmail);
		return ResponseEntity.ok(list);
	}

	/**
	 * It is for get list of events
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/eventlist")
	public ResponseEntity<?> eventlist(@RequestHeader HttpHeaders headers) {
		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		Collection<EventSetup> list = eventManageService.findAllDetailEventsByUser(userEmail);
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/myactivesessionlist")
	public ResponseEntity<?> myActiveSessions(@RequestHeader HttpHeaders headers) {
		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		Collection<EventSetup> list = eventManageService.myActiveSessionsByUsers(userEmail);
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/activesessionlist")
	public ResponseEntity<?> activeSessions(@RequestHeader HttpHeaders headers) {
		Collection<EventSetup> list = eventManageService.findEventsBySessionStatus();
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/mysessionlist")
	public ResponseEntity<?> mySessions(@RequestHeader HttpHeaders headers) {

		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		Collection<EventSetup> list = eventManageService.findEventsBySessionStatusByUsers(userEmail);
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/endsessionlist")
	public ResponseEntity<?> endSessions(@RequestHeader HttpHeaders headers) {
		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		Collection<EventSetup> list = eventManageService.findEndEventsBySessionStatusByUsers(userEmail);
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/eventplacelist")
	public ResponseEntity<?> locationList(@RequestHeader HttpHeaders headers) {
		Collection<EventPlace> list = eventManageService.findAllPlaces();
		return ResponseEntity.ok(list);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */

	@GetMapping("/events/{eventid}")
	public ResponseEntity<?> eventDetail(@PathVariable Long eventid) {
		EventSetup e1 = eventManageService.getEventSessionRecord(eventid);
		return ResponseEntity.ok(e1);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/eventdetails/{eventid}")
	public ResponseEntity<EventDetailSummaryResponse> eventDetailSummary(@PathVariable Long eventid) {
		EventDetailSummaryResponse eventDetailSummaryResponse = eventManageService.getEventDetailSummaryRecord(eventid);
		return ResponseEntity.ok(eventDetailSummaryResponse);
	}

	/**
	 * 
	 * @param headers
	 * @return
	 */
	@GetMapping("/myeventdetail/{eventid}")
	public ResponseEntity<EventDetailSummaryResponse> myEventDetail(@RequestHeader HttpHeaders headers,
			@PathVariable Long eventid) {
		EventDetailSummaryResponse eventDetailSummaryResponse = eventManageService.myEventDetailRecord(eventid);

		return ResponseEntity.ok(eventDetailSummaryResponse);
	}

	@GetMapping("/joinevents/{eventid}")
	public ResponseEntity<?> joinEvents(@RequestHeader HttpHeaders headers, @PathVariable Long eventid) {

		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));

		EventSetup eventSetup = eventManageService.joinEvent(eventid, userEmail);

		return ResponseEntity.ok(eventSetup);
	}

	@GetMapping("/endEvent/{eventid}")
	public ResponseEntity<?> endEvents(@RequestHeader HttpHeaders headers, @PathVariable Long eventid) {
		EventInviteSummary propsal = eventManageService.endEventSubmit(eventid);
		return ResponseEntity.ok(propsal);
	}

	@PostMapping("/createPlacePropose")
	public ResponseEntity<?> placeCreateWithPropose(@RequestHeader HttpHeaders headers,
			@Valid @RequestBody EventPlaceRequest eventSetupRequest) {
		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));

		EventProposedLocation eventProposedLocation = eventManageService.createPlaceProposeLocation(eventSetupRequest,
				userEmail);

		return ResponseEntity.ok(eventProposedLocation);
	}

	@PostMapping("/createProposeLoation")
	public ResponseEntity<?> createProposeLoation(@RequestHeader HttpHeaders headers,
			@Valid @RequestBody EventPlaceRequest eventSetupRequest) {

		String userEmail = getUserEmailFromUserToken(headers.get("Authorization").get(0));
		EventProposedLocation eventProposedLocation = eventManageService.createProposeLocation(eventSetupRequest,
				userEmail);
		return ResponseEntity.ok(eventProposedLocation);
	}

}
