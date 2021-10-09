package com.twitchcollector.callbackconsumer.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    private final Metadata metadata;
    private final Object data;

    public Event(@JsonProperty("metadata") Metadata metadata, @JsonProperty("data") Object data) {
        this.metadata = metadata;
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Object getData() {
        return data;
    }
}
