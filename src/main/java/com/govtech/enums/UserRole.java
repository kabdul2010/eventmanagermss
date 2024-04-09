package com.govtech.enums;

public enum UserRole {


	ROLE_SUPERADMIN("ROLE_SUPERADMIN"), ROLE_CLIENT("ROLE_CLIENT"), ROLE_USER("ROLE_USER");

	private String value;

	private UserRole(String value) {

		this.value=value;
	}

	public String getValue() {

		return this.value;
	}

}
