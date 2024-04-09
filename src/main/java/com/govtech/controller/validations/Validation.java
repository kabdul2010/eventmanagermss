package com.govtech.controller.validations;

import com.govtech.constant.AppConstant;

public final class Validation {

    private Validation() {

    }

    public static boolean validateMobileNumber(String mobileNumber) {

        return mobileNumber.matches(AppConstant.VALIDATE_MOBILE_NUMBER_REGEX);
    }

    public static boolean validateEmailId(String emailId) {
        return emailId.matches(AppConstant.VALIDATE_EMAIL_REGEX);
    }

	/*
	 * public static List<String> validateClientDTO(ClientDTO clientDTO) {
	 * 
	 * List<String> errors = new ArrayList<>(); if
	 * (!validateEmailId(clientDTO.getEmailId())) {
	 * errors.add("Email Id is not valid"); } else if
	 * (!validateMobileNumber(clientDTO.getMobile())) {
	 * errors.add("Mobile number should contain 10 digits"); } return errors; }
	 */

}
