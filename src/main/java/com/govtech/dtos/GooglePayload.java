package com.govtech.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GooglePayload {
	
	private String iss;
	
	private String email;
	
	private String azp;
	
	private String aud;
	
	private String sub;
	
	private String hd;
	
	private Boolean email_verified;
	
	private Long nbf;
	
	private String name;
	
	private String picture;
	
	private String given_name;
	
	private String family_name;
	
	private Long exp;
	
	private Long iat;
	
	private String locale;
	
	private String jti;

}
