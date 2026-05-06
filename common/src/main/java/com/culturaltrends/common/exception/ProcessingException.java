package com.culturaltrends.common.exception;

public class ProcessingException extends RuntimeException {

    private final String eventId;

    public ProcessingException(String eventId, String message) {
        super(message);
        this.eventId = eventId;
    }

    public ProcessingException(String eventId, String message, Throwable cause) {
        super(message, cause);
        this.eventId = eventId;
    }

    public String getEventId() { return eventId; }
}
