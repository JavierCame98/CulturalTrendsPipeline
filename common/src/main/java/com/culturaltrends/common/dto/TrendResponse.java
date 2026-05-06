package com.culturaltrends.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record TrendResponse(
        @JsonProperty("event_id")      String eventId,
        @JsonProperty("source")        String source,
        @JsonProperty("title")         String title,
        @JsonProperty("url")           String url,
        @JsonProperty("category")      String category,
        @JsonProperty("trend_score")   double trendScore,
        @JsonProperty("mention_count") long mentionCount,
        @JsonProperty("ingested_at")   Instant ingestedAt
) {}
