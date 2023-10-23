package com.in28Minutes.rest.webservices.restfulwebservices.exception
        ;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ErrorDetails {
//    timestamp
//    message
//    details

    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    private LocalDateTime timestamp;
    private String message;
    private String details;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }



}
