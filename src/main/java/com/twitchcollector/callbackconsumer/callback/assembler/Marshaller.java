package com.twitchcollector.callbackconsumer.callback.assembler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Marshaller {

    private final ObjectMapper objectMapper;

    public Marshaller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String marshal(Object Object) {
        try {
            return objectMapper.writeValueAsString(Object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
