# CulturalTrendsPipeline

## Contratos de eventos Kafka

### Topics

| Topic | Producer | Consumer | Descripción |
|---|---|---|---|
| `cultural-events-raw` | ingestion-service | processing-service | Eventos culturales sin procesar |
| `cultural-trends-dlt` | processing-service | (monitorización) | Mensajes que fallaron tras 3 reintentos |

### Esquema de `CulturalEvent`

Todos los mensajes del topic `cultural-events-raw` siguen este esquema JSON:

\`\`\`json
{
"event_id":      "f47ac10b-58cc-4372-a567-0e02b2c3d479",  // UUID único del evento
"source":        "wikipedia",                              // fuente: wikipedia | eventbrite | spotify
"title":         "Frida Kahlo",                            // título del evento cultural
"url":           "https://en.wikipedia.org/wiki/Frida_Kahlo",
"raw_category":  "biography",                              // categoría sin normalizar
"mention_count": 15432,                                    // visitas / menciones / reproducciones
"description":   "Mexican painter known for self-portraits", // opcional
"ingested_at":   "2025-03-15T14:30:00Z"                   // ISO-8601 UTC
}
\`\`\`

### Reglas de compatibilidad

- Los nombres de campo JSON (`snake_case`) son contratos fijos.
  Cambiarlos sin coordinación entre servicios rompe la comunicación.
- `mention_count` siempre es >= 0.
- `ingested_at` siempre es UTC. Nunca incluir offset de zona horaria.
- `raw_category` puede ser `null` si la fuente no proporciona categoría.
- `description` es opcional. El consumer debe tolerarlo como `null`.