package com.twitchcollector.callbackconsumer.callback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twitchcollector.callbackconsumer.callback.rest.resource.CallbackResource;

public class NotificationReceivedV1 {

    private final String messageId;
    private final Integer messageRetry;
    private final String messageType;
    private final String messageSignature;
    private final String messageTimestamp;
    private final String subscriptionType;
    private final Integer subscriptionVersion;
    private final CallbackResource callback;

    public NotificationReceivedV1(@JsonProperty("messageId") String messageId,
                                  @JsonProperty("messageRetry") Integer messageRetry,
                                  @JsonProperty("messageType") String messageType,
                                  @JsonProperty("messageSignature") String messageSignature,
                                  @JsonProperty("messageTimestamp") String messageTimestamp,
                                  @JsonProperty("subscriptionType") String subscriptionType,
                                  @JsonProperty("subscriptionVersion") Integer subscriptionVersion,
                                  @JsonProperty("callback") CallbackResource callback) {
        this.messageId = messageId;
        this.messageRetry = messageRetry;
        this.messageType = messageType;
        this.messageSignature = messageSignature;
        this.messageTimestamp = messageTimestamp;
        this.subscriptionType = subscriptionType;
        this.subscriptionVersion = subscriptionVersion;
        this.callback = callback;
    }

    public String getMessageId() {
        return messageId;
    }

    public Integer getMessageRetry() {
        return messageRetry;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageSignature() {
        return messageSignature;
    }

    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Integer getSubscriptionVersion() {
        return subscriptionVersion;
    }

    public CallbackResource getCallback() {
        return callback;
    }
}
