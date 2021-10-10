package com.twitchcollector.callbackconsumer.callback;

import com.twitchcollector.callbackconsumer.callback.assembler.EventAssembler;
import com.twitchcollector.callbackconsumer.callback.assembler.Marshaller;
import com.twitchcollector.callbackconsumer.callback.dto.ChallengeReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.NotificationReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.RevocationReceivedV1;
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

    public void saveNotificationReceived(NotificationReceivedV1 notificationReceived) {
        final var messageId = notificationReceived.getMessageId();
        final var userId = notificationReceived.getCallback().getSubscription().getCondition().get("broadcaster_user_id");
        logger.debug("Saving notification received event with messageId: {} for userId: {}", messageId, userId);
        final var event = eventAssembler.assemble("NOTIFICATION_RECEIVED", "1", notificationReceived);
        final var eventJson = marshaller.marshal(event);
        kafkaProducer.produce(userId, eventJson);
        logger.info("Saved notification received event with messageId: {} for userId: {}", messageId, userId);
    }

    public void saveRevocationReceived(RevocationReceivedV1 revocationReceived) {
        final var messageId = revocationReceived.getMessageId();
        final var userId = revocationReceived.getCallback().getSubscription().getCondition().get("broadcaster_user_id");
        logger.debug("Saving revocation received event with messageId: {} for userId: {}", messageId, userId);
        final var event = eventAssembler.assemble("REVOCATION_RECEIVED", "1", revocationReceived);
        final var eventJson = marshaller.marshal(event);
        kafkaProducer.produce(userId, eventJson);
        logger.info("Saved revocation received event with messageId: {} for userId: {}", messageId, userId);
    }

    public void saveChallengeReceived(ChallengeReceivedV1 challengeReceived) {
        final var messageId = challengeReceived.getMessageId();
        final var userId = challengeReceived.getCallback().getSubscription().getCondition().get("broadcaster_user_id");
        logger.debug("Saving challenge received event with messageId: {} for userId: {}", messageId, userId);
        final var event = eventAssembler.assemble("CHALLENGE_RECEIVED", "1", challengeReceived);
        final var eventJson = marshaller.marshal(event);
        kafkaProducer.produce(userId, eventJson);
        logger.info("Saved challenge received event with messageId: {} for userId: {}", messageId, userId);
    }
}
