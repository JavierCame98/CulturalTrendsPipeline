package com.culturaltrends.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

/**
 * Contrato de datos compartido entre ingestion-service (producer) y processing-service (consumer).
 * Viaja serializado como JSON por el topic Kafka {@code cultural-events-raw}.
 * <p>
 * Los nombres de campo JSON son un contrato irrompible: si cambian,
 * el consumer recibirá {@code null} silenciosamente en lugar de un error.
 */
@Builder
@JsonDeserialize(builder = CulturalEvent.CulturalEventBuilder.class)
public record CulturalEvent(

        /** JSON: {@code event_id} — UUID autogenerado al momento de creación. */
        @JsonProperty("event_id")
        UUID eventId,

        /** JSON: {@code source} — Fuente del evento: wikipedia, eventbrite, spotify. */
        @NotBlank
        @JsonProperty("source")
        String source,

        /** JSON: {@code title} — Título del evento cultural. */
        @NotBlank
        @JsonProperty("title")
        String title,

        /** JSON: {@code url} — URL canónica del recurso original. */
        @JsonProperty("url")
        String url,

        /** JSON: {@code raw_category} — Categoría sin normalizar tal como viene de la fuente. */
        @JsonProperty("raw_category")
        String rawCategory,

        /** JSON: {@code mention_count} — Visitas, menciones o reproducciones. */
        @Min(0)
        @JsonProperty("mention_count")
        long mentionCount,

        /** JSON: {@code description} — Descripción breve opcional. */
        @JsonProperty("description")
        String description,

        /** JSON: {@code ingested_at} — Timestamp UTC de creación del evento. */
        @NotNull
        @JsonProperty("ingested_at")
        Instant ingestedAt

) {
    /**
     * Builder personalizado que aporta valores por defecto para {@code eventId} e {@code ingestedAt},
     * y permite a Jackson deserializar usando el builder en lugar de un constructor vacío.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class CulturalEventBuilder {
        private UUID eventId = UUID.randomUUID();
        private Instant ingestedAt = Instant.now();
    }
}
