package com.culturaltrends.common;

import com.culturaltrends.common.event.CulturalEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CulturalEventTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void builderGeneratesEventIdAndIngestedAtAutomatically() {
        CulturalEvent event = CulturalEvent.builder()
                .source("wikipedia")
                .title("Festival de Jazz")
                .build();

        assertNotNull(event.eventId(), "eventId debe ser autogenerado");
        assertNotNull(event.ingestedAt(), "ingestedAt debe ser autogenerado");
    }

    @Test
    void twoInstancesHaveDifferentEventId() {
        CulturalEvent first  = CulturalEvent.builder().source("spotify").title("Album X").build();
        CulturalEvent second = CulturalEvent.builder().source("spotify").title("Album Y").build();

        assertNotEquals(first.eventId(), second.eventId(),
                "Dos instancias distintas deben tener eventId diferente");
    }

    @Test
    void serializesToJsonWithSnakeCaseFieldNames() throws Exception {
        CulturalEvent event = CulturalEvent.builder()
                .source("eventbrite")
                .title("Obra de teatro")
                .url("https://eventbrite.com/e/123")
                .rawCategory("performing-arts")
                .mentionCount(500L)
                .description("Una obra clásica")
                .build();

        String json = mapper.writeValueAsString(event);

        assertTrue(json.contains("\"event_id\""),     "debe serializar como event_id");
        assertTrue(json.contains("\"ingested_at\""),  "debe serializar como ingested_at");
        assertTrue(json.contains("\"raw_category\""), "debe serializar como raw_category");
        assertTrue(json.contains("\"mention_count\""),"debe serializar como mention_count");
        assertTrue(json.contains("\"source\""),       "debe incluir source");
        assertTrue(json.contains("\"title\""),        "debe incluir title");
    }

    @Test
    void deserializesFromJsonCorrectly() throws Exception {
        String json = """
                {
                  "event_id": "550e8400-e29b-41d4-a716-446655440000",
                  "source": "wikipedia",
                  "title": "Exposición de Arte Moderno",
                  "url": "https://es.wikipedia.org/wiki/Arte_moderno",
                  "raw_category": "art",
                  "mention_count": 1200,
                  "description": "Gran exposición en Madrid",
                  "ingested_at": "2024-01-15T10:30:00Z"
                }
                """;

        CulturalEvent event = mapper.readValue(json, CulturalEvent.class);

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), event.eventId());
        assertEquals("wikipedia",                           event.source());
        assertEquals("Exposición de Arte Moderno",         event.title());
        assertEquals("https://es.wikipedia.org/wiki/Arte_moderno", event.url());
        assertEquals("art",                                event.rawCategory());
        assertEquals(1200L,                                event.mentionCount());
        assertEquals("Gran exposición en Madrid",          event.description());
        assertEquals(Instant.parse("2024-01-15T10:30:00Z"), event.ingestedAt());
    }

    @Test
    void roundTripProducesSameObject() throws Exception {
        CulturalEvent original = CulturalEvent.builder()
                .source("spotify")
                .title("Billie Eilish - Hit Me Hard and Soft")
                .url("https://open.spotify.com/album/abc123")
                .rawCategory("music")
                .mentionCount(5_000_000L)
                .description("Álbum más escuchado del mes")
                .ingestedAt(Instant.parse("2024-06-01T00:00:00Z"))
                .build();

        String json = mapper.writeValueAsString(original);
        CulturalEvent deserialized = mapper.readValue(json, CulturalEvent.class);

        // Los records generan equals() por valor automáticamente
        assertEquals(original, deserialized);
    }
}
