package com.culturaltrends.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record TrendSummary(
        @JsonProperty("trends")       List<TrendResponse> trends,
        @JsonProperty("total")        int total,
        @JsonProperty("source")       String source,
        @JsonProperty("generated_at") Instant generatedAt
) {
    public static TrendSummary of(List<TrendResponse> trends, String source) {
        return new TrendSummary(trends, trends.size(), source, Instant.now());
    }
}
