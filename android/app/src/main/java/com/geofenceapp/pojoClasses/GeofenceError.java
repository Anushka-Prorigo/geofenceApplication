package com.geofenceapp.pojoclasses;

import com.geofenceapp.constants.GeofenceErrorCodes;

public class GeofenceError {
    private GeofenceErrorCodes code;
    private String message;

    public GeofenceError(GeofenceErrorCodes code, String message) {
        this.code = code;
        this.message = message;
    }
 
    public GeofenceErrorCodes getCode() {
        return code;
    }
 
    public String getMessage() {
        return message;
    }
}