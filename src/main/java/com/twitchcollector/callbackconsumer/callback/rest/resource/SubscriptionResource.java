package com.twitchcollector.callbackconsumer.callback.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SubscriptionResource {

    private final String id;
    private final String type;
    private final String version;
    private final String status;
    private final Integer cost;
    private final Map<String, String> condition;
    private final TransportResource transport;
    private final String createdAt;

    public SubscriptionResource(@JsonProperty("id") String id,
                                @JsonProperty("type") String type,
                                @JsonProperty("version") String version,
                                @JsonProperty("status") String status,
                                @JsonProperty("cost") Integer cost,
                                @JsonProperty("condition") Map<String, String> condition,
                                @JsonProperty("transport") TransportResource transport,
                                @JsonProperty("created_at") String createdAt) {
        this.id = id;
        this.type = type;
        this.version = version;
        this.status = status;
        this.cost = cost;
        this.condition = condition;
        this.transport = transport;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCost() {
        return cost;
    }

    public Map<String, String> getCondition() {
        return condition;
    }

    public TransportResource getTransport() {
        return transport;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
