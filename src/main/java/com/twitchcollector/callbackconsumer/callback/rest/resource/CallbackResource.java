package com.twitchcollector.callbackconsumer.callback.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class CallbackResource {

    private final String challenge;
    private final SubscriptionResource subscription;
    private final EventResource event;

    public CallbackResource(@JsonProperty("challenge") String challenge, @JsonProperty("subscription") SubscriptionResource subscription, @JsonProperty("event") EventResource event) {
        this.challenge = challenge;
        this.subscription = subscription;
        this.event = event;
    }

    public Optional<String> getChallenge() {
        return Optional.ofNullable(challenge);
    }

    public SubscriptionResource getSubscription() {
        return subscription;
    }

    public Optional<EventResource> getEvent() {
        return Optional.ofNullable(event);
    }
}
