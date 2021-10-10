package com.twitchcollector.callbackconsumer.callback.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitchcollector.callbackconsumer.callback.CallbackService;
import com.twitchcollector.callbackconsumer.callback.dto.ChallengeReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.NotificationReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.RevocationReceivedV1;
import com.twitchcollector.callbackconsumer.callback.rest.resource.CallbackResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("callback")
public class CallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    private final CallbackService callbackService;
    private final ObjectMapper objectMapper;

    public CallbackController(CallbackService callbackService, ObjectMapper objectMapper) {
        this.callbackService = callbackService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> receiveCallback(@RequestHeader("Twitch-EventSub-Message-Id") String messageId,
                                             @RequestHeader("Twitch-EventSub-Message-Retry") Integer messageRetry,
                                             @RequestHeader("Twitch-EventSub-Message-Type") String messageType,
                                             @RequestHeader("Twitch-EventSub-Message-Signature") String messageSignature,
                                             @RequestHeader("Twitch-EventSub-Message-Timestamp") String messageTimestamp,
                                             @RequestHeader("Twitch-EventSub-Subscription-Type") String subscriptionType,
                                             @RequestHeader("Twitch-EventSub-Subscription-Version") Integer subscriptionVersion,
                                             @RequestBody CallbackResource resource) throws JsonProcessingException {
        switch (messageType) {
            case "notification" -> {
                final var notificationReceived = new NotificationReceivedV1(messageId, messageRetry, messageType, messageSignature, messageTimestamp, subscriptionType, subscriptionVersion, resource);
                callbackService.saveNotificationReceived(notificationReceived);
                return ResponseEntity.ok().build();
            }
            case "revocation" -> {
                final var revocationReceived = new RevocationReceivedV1(messageId, messageRetry, messageType, messageSignature, messageTimestamp, subscriptionType, subscriptionVersion, resource);
                callbackService.saveRevocationReceived(revocationReceived);
                return ResponseEntity.ok().build();
            }
            case "webhook_callback_verification" -> {
                final var challengeReceived = new ChallengeReceivedV1(messageId, messageRetry, messageType, messageSignature, messageTimestamp, subscriptionType, subscriptionVersion, resource);
                callbackService.saveChallengeReceived(challengeReceived);
                logger.info("Returning challenge: {} for subscription id: {}", resource.getChallenge(), resource.getSubscription().getId());
                return ResponseEntity.ok(resource.getChallenge().orElseThrow());
            }
            default -> {
                logger.info("Unhandled messageType: {} with payload: {}", messageType, objectMapper.writeValueAsString(resource));
                return ResponseEntity.ok().build();
            }
        }
    }
}
