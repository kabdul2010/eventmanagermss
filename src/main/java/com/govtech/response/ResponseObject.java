package com.govtech.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseObject {

    private String message;

    private ResponseObject(String message) {
        this.message = message;
    }

    public static ResponseObject success(String message) {
        return new ResponseObject(message);
    }

}
