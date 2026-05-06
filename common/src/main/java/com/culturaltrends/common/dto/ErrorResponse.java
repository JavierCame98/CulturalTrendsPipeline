package com.culturaltrends.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ErrorResponse(
        @JsonProperty("status")    int status,
        @JsonProperty("error")     String error,
        @JsonProperty("message")   String message,
        @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("path")      String path
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, error, message, Instant.now(), path);
    }
}
