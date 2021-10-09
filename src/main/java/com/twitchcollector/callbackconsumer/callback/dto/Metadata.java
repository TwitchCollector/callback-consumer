package com.twitchcollector.callbackconsumer.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {

    private final String eventId;
    private final String correlationId;
    private final String eventType;
    private final String version;
    private final String created;

    public Metadata(@JsonProperty("eventId") String eventId,
                    @JsonProperty("correlationId") String correlationId,
                    @JsonProperty("eventType") String eventType,
                    @JsonProperty("version") String version,
                    @JsonProperty("created") String created) {
        this.eventId = eventId;
        this.correlationId = correlationId;
        this.eventType = eventType;
        this.version = version;
        this.created = created;
    }

    public String getEventId() {
        return eventId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getVersion() {
        return version;
    }

    public String getCreated() {
        return created;
    }
}
