package com.govtech.validateroles;

import com.govtech.enums.UserRole;

public class UserRoleValidator {

	public static boolean validateRole(String role){

		return isValidRole(role);
	}
	
	private static boolean isValidRole(String role){
		
		System.out.println("in validation");
		
		UserRole[] values = UserRole.values();

		for(UserRole validRole:values){
			if(validRole.getValue().equalsIgnoreCase(role)){
				
				return true;
			}
		}
		
		return false;
	}
}
