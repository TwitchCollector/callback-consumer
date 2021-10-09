package com.twitchcollector.callbackconsumer.callback.assembler;

import com.twitchcollector.callbackconsumer.callback.dto.Event;
import com.twitchcollector.callbackconsumer.callback.dto.Metadata;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class EventAssembler {

    private final Supplier<String> uuidSupplier;
    private final Supplier<String> timeSupplier;

    public EventAssembler(Supplier<String> uuidSupplier, Supplier<String> timeSupplier) {
        this.uuidSupplier = uuidSupplier;
        this.timeSupplier = timeSupplier;
    }

    public Event assemble(String eventType, String version, Object data) {
        return new Event(new Metadata(uuidSupplier.get(), uuidSupplier.get(), eventType, version, timeSupplier.get()), data);
    }
}
