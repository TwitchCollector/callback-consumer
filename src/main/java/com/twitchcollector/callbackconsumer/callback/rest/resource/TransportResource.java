package com.twitchcollector.callbackconsumer.callback.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransportResource {

    private final String method;
    private final String callback;

    public TransportResource(@JsonProperty("method") String method, @JsonProperty("callback") String callback) {
        this.method = method;
        this.callback = callback;
    }

    public String getMethod() {
        return method;
    }

    public String getCallback() {
        return callback;
    }
}
