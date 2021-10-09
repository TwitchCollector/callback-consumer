package com.twitchcollector.callbackconsumer.callback;

import com.twitchcollector.callbackconsumer.callback.assembler.EventAssembler;
import com.twitchcollector.callbackconsumer.callback.assembler.Marshaller;
import com.twitchcollector.callbackconsumer.callback.dto.NotificationV1;
import com.twitchcollector.callbackconsumer.callback.dto.RevocationV1;
import com.twitchcollector.callbackconsumer.callback.kafka.CallbackProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CallbackService {

    private static final Logger logger = LoggerFactory.getLogger(CallbackService.class);

    private final EventAssembler eventAssembler;
    private final Marshaller marshaller;
    private final CallbackProducer kafkaProducer;

    public CallbackService(EventAssembler eventAssembler, Marshaller marshaller, CallbackProducer kafkaProducer) {
        this.eventAssembler = eventAssembler;
        this.marshaller = marshaller;
        this.kafkaProducer = kafkaProducer;
    }

    public void saveNotification(NotificationV1 notification) {
        final var messageId = notification.getMessageId();
        final var userId = notification.getCallback().getSubscription().getCondition().get("broadcaster_user_id");
        logger.debug("Saving notification with messageId: {} for userId: {}", messageId, userId);
        final var event = eventAssembler.assemble("NOTIFICATION_RECEIVED", "1", notification);
        final var eventJson = marshaller.marshal(event);
        kafkaProducer.produce(userId, eventJson);
        logger.info("Saved notification with messageId: {} for userId: {}", messageId, userId);
    }

    public void saveRevocation(RevocationV1 revocation) {
        final var messageId = revocation.getMessageId();
        final var userId = revocation.getCallback().getSubscription().getCondition().get("broadcaster_user_id");
        logger.debug("Saving revocation with messageId: {} for userId: {}", messageId, userId);
        final var event = eventAssembler.assemble("REVOCATION_RECEIVED", "1", revocation);
        final var eventJson = marshaller.marshal(event);
        kafkaProducer.produce(userId, eventJson);
        logger.info("Saved revocation with messageId: {} for userId: {}", messageId, userId);
    }
}
