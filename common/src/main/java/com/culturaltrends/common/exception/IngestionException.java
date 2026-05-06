package com.culturaltrends.common.exception;

public class IngestionException extends RuntimeException {

    private final String source;

    public IngestionException(String source, String message) {
        super(message);
        this.source = source;
    }

    public IngestionException(String source, String message, Throwable cause) {
        super(message, cause);
        this.source = source;
    }

    public String getSource() { return source; }
}
