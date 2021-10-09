package com.twitchcollector.callbackconsumer.callback.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EventResource {

    private final String id;
    private final String broadcasterUserId;
    private final String broadcasterUserLogin;
    private final String broadcasterUsername;
    private final String type;
    private final String startedAt;

    public EventResource(@JsonProperty("id") String id,
                         @JsonProperty("broadcaster_user_id") String broadcasterUserId,
                         @JsonProperty("broadcaster_user_login") String broadcasterUserLogin,
                         @JsonProperty("broadcaster_user_name") String broadcasterUsername,
                         @JsonProperty("type") String type,
                         @JsonProperty("started_at") String startedAt) {
        this.id = id;
        this.broadcasterUserId = broadcasterUserId;
        this.broadcasterUserLogin = broadcasterUserLogin;
        this.broadcasterUsername = broadcasterUsername;
        this.type = type;
        this.startedAt = startedAt;
    }

    public String getId() {
        return id;
    }

    public String getBroadcasterUserId() {
        return broadcasterUserId;
    }

    public String getBroadcasterUserLogin() {
        return broadcasterUserLogin;
    }

    public String getBroadcasterUsername() {
        return broadcasterUsername;
    }

    public String getType() {
        return type;
    }

    public String getStartedAt() {
        return startedAt;
    }
}
