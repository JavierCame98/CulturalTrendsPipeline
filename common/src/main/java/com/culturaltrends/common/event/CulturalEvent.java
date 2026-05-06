package com.culturaltrends.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Contrato de datos compartido entre ingestion-service (producer) y processing-service (consumer).
 * Viaja serializado como JSON por el topic Kafka {@code cultural-events-raw}.
 * <p>
 * Los nombres de campo JSON son un contrato irrompible: si cambian,
 * el consumer recibirá {@code null} silenciosamente en lugar de un error.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = CulturalEvent.CulturalEventBuilder.class)
public class CulturalEvent {

    /** JSON: {@code event_id} — UUID autogenerado al momento de creación. */
    @Builder.Default
    @JsonProperty("event_id")
    private String eventId = UUID.randomUUID().toString();

    /** JSON: {@code source} — Fuente del evento: wikipedia, eventbrite, spotify. */
    @NotBlank
    @JsonProperty("source")
    private String source;

    /** JSON: {@code title} — Título del evento cultural. */
    @NotBlank
    @JsonProperty("title")
    private String title;

    /** JSON: {@code url} — URL canónica del recurso original. */
    @JsonProperty("url")
    private String url;

    /** JSON: {@code raw_category} — Categoría sin normalizar tal como viene de la fuente. */
    @JsonProperty("raw_category")
    private String rawCategory;

    /** JSON: {@code mention_count} — Visitas, menciones o reproducciones. */
    @Min(0)
    @JsonProperty("mention_count")
    private long mentionCount;

    /** JSON: {@code description} — Descripción breve opcional. */
    @JsonProperty("description")
    private String description;

    /** JSON: {@code ingested_at} — Timestamp UTC de creación del evento. */
    @NotNull
    @Builder.Default
    @JsonProperty("ingested_at")
    private Instant ingestedAt = Instant.now();

    /**
     * Builder declarado explícitamente para poder anotar con {@code @JsonPOJOBuilder},
     * que permite a Jackson usar el builder para deserializar sin necesitar el constructor vacío.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class CulturalEventBuilder {}
}
