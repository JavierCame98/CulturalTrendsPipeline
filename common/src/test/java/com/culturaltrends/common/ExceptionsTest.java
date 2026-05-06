package com.culturaltrends.common;

import com.culturaltrends.common.exception.IngestionException;
import com.culturaltrends.common.exception.ProcessingException;
import com.culturaltrends.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionsTest {

    @Test
    void ingestionException_preservesSourceAndMessage() {
        var ex = new IngestionException("spotify", "API rate limit exceeded");
        assertEquals("spotify", ex.getSource());
        assertEquals("API rate limit exceeded", ex.getMessage());
    }

    @Test
    void ingestionException_withCause_preservesCause() {
        var cause = new RuntimeException("connection timeout");
        var ex = new IngestionException("spotify", "API rate limit exceeded", cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    void processingException_preservesEventIdAndMessage() {
        var ex = new ProcessingException("evt-123", "Failed to normalize event");
        assertEquals("evt-123", ex.getEventId());
        assertEquals("Failed to normalize event", ex.getMessage());
    }

    @Test
    void resourceNotFoundException_buildsMsgFromTypeAndId() {
        var ex = new ResourceNotFoundException("Trend", "trend-456");
        assertEquals("Trend", ex.getResourceType());
        assertEquals("trend-456", ex.getResourceId());
        assertEquals("Trend not found: trend-456", ex.getMessage());
    }
}
