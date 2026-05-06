package com.culturaltrends.common;

import com.culturaltrends.common.dto.ErrorResponse;
import com.culturaltrends.common.dto.TrendResponse;
import com.culturaltrends.common.dto.TrendSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrendResponseTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void trendSummaryOfCalculatesTotalFromListSize() {
        List<TrendResponse> trends = List.of(
                new TrendResponse("id-1", "wikipedia", "Jazz Festival", "https://example.com", "music", 8.5, 1200L, Instant.now()),
                new TrendResponse("id-2", "spotify",   "Art Expo",     "https://example.com", "art",   6.0,  800L, Instant.now())
        );

        TrendSummary summary = TrendSummary.of(trends, "wikipedia");

        assertEquals(2, summary.total(), "total debe ser igual al tamaño de la lista");
    }

    @Test
    void trendSummaryOfGeneratesGeneratedAtAutomatically() {
        TrendSummary summary = TrendSummary.of(List.of(), null);

        assertNotNull(summary.generatedAt(), "generatedAt debe ser autogenerado");
    }

    @Test
    void trendResponseSerializesToJsonWithSnakeCaseFieldNames() throws Exception {
        TrendResponse trend = new TrendResponse(
                "abc-123",
                "eventbrite",
                "Obra de teatro",
                "https://eventbrite.com/e/123",
                "performing-arts",
                7.3,
                500L,
                Instant.parse("2024-03-10T12:00:00Z")
        );

        String json = mapper.writeValueAsString(trend);

        assertTrue(json.contains("\"event_id\""),      "debe serializar como event_id");
        assertTrue(json.contains("\"trend_score\""),   "debe serializar como trend_score");
        assertTrue(json.contains("\"mention_count\""), "debe serializar como mention_count");
        assertTrue(json.contains("\"ingested_at\""),   "debe serializar como ingested_at");
        assertTrue(json.contains("\"source\""),        "debe incluir source");
        assertTrue(json.contains("\"category\""),      "debe incluir category");
    }

    @Test
    void errorResponseOfGeneratesTimestampAutomatically() {
        ErrorResponse error = ErrorResponse.of(404, "Not Found", "Recurso no encontrado", "/api/trends/xyz");

        assertNotNull(error.timestamp(), "timestamp debe ser autogenerado");
        assertEquals(404,             error.status());
        assertEquals("Not Found",     error.error());
        assertEquals("/api/trends/xyz", error.path());
    }
}
