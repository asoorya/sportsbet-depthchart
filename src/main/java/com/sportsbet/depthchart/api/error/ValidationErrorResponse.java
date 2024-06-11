package com.sportsbet.depthchart.api.error;

import java.util.List;

public class ValidationErrorResponse {
    private String message;
    private List<String> details;

    public ValidationErrorResponse(String message, List<String> details) {
        this.message = message;
        this.details = details;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
