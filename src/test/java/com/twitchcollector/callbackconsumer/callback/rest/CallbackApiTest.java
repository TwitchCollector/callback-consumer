package com.twitchcollector.callbackconsumer.callback.rest;

import com.twitchcollector.callbackconsumer.callback.CallbackService;
import com.twitchcollector.callbackconsumer.callback.dto.ChallengeReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.NotificationReceivedV1;
import com.twitchcollector.callbackconsumer.callback.dto.RevocationReceivedV1;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.twitchcollector.callbackconsumer.FileUtil.fileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.boot.admin.client.enabled=false"})
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
class CallbackApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CallbackService callbackServiceMock;

    @MockBean
    private KafkaAdmin kafkaAdminMock;

    @Test
    void POST_callback_with_stream_online_notification_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(NotificationReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "notification")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.online")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-notification-stream-online.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveNotificationReceived(captor.capture());

        final var notificationReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", notificationReceived.getMessageId());
        assertEquals(0, notificationReceived.getMessageRetry());
        assertEquals("notification", notificationReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", notificationReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", notificationReceived.getMessageTimestamp());
        assertEquals("stream.online", notificationReceived.getSubscriptionType());
        assertEquals(1, notificationReceived.getSubscriptionVersion());

        final var callback = notificationReceived.getCallback();

        assertTrue(callback.getChallenge().isEmpty());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("enabled", subscription.getStatus());
        assertEquals("stream.online", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(0, subscription.getCost());
        assertEquals("1337", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isPresent());
        final var event = callback.getEvent().get();
        assertEquals("9001", event.getId());
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserLogin());
        assertEquals("Cool_User", event.getBroadcasterUsername());
        assertEquals("live", event.getType());
        assertEquals("2020-10-11T10:11:12.123Z", event.getStartedAt());
    }

    @Test
    void POST_callback_with_stream_offline_notification_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(NotificationReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "notification")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.offline")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-notification-stream-offline.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveNotificationReceived(captor.capture());

        final var notificationReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", notificationReceived.getMessageId());
        assertEquals(0, notificationReceived.getMessageRetry());
        assertEquals("notification", notificationReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", notificationReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", notificationReceived.getMessageTimestamp());
        assertEquals("stream.offline", notificationReceived.getSubscriptionType());
        assertEquals(1, notificationReceived.getSubscriptionVersion());

        final var callback = notificationReceived.getCallback();

        assertTrue(callback.getChallenge().isEmpty());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("enabled", subscription.getStatus());
        assertEquals("stream.offline", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(0, subscription.getCost());
        assertEquals("1337", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isPresent());
        final var event = callback.getEvent().get();
        assertEquals("1337", event.getBroadcasterUserId());
        assertEquals("cool_user", event.getBroadcasterUserLogin());
        assertEquals("Cool_User", event.getBroadcasterUsername());
    }

    @Test
    void POST_callback_with_stream_online_revocation_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(RevocationReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "revocation")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.online")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-revocation-stream-online.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveRevocationReceived(captor.capture());

        final var revocationReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", revocationReceived.getMessageId());
        assertEquals(0, revocationReceived.getMessageRetry());
        assertEquals("revocation", revocationReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", revocationReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", revocationReceived.getMessageTimestamp());
        assertEquals("stream.online", revocationReceived.getSubscriptionType());
        assertEquals(1, revocationReceived.getSubscriptionVersion());

        final var callback = revocationReceived.getCallback();

        assertTrue(callback.getChallenge().isEmpty());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("authorization_revoked", subscription.getStatus());
        assertEquals("stream.online", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(1, subscription.getCost());
        assertEquals("12826", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isEmpty());
    }

    @Test
    void POST_callback_with_stream_offline_revocation_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(RevocationReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "revocation")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.offline")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-revocation-stream-offline.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveRevocationReceived(captor.capture());

        final var revocationReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", revocationReceived.getMessageId());
        assertEquals(0, revocationReceived.getMessageRetry());
        assertEquals("revocation", revocationReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", revocationReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", revocationReceived.getMessageTimestamp());
        assertEquals("stream.offline", revocationReceived.getSubscriptionType());
        assertEquals(1, revocationReceived.getSubscriptionVersion());

        final var callback = revocationReceived.getCallback();

        assertTrue(callback.getChallenge().isEmpty());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("authorization_revoked", subscription.getStatus());
        assertEquals("stream.offline", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(1, subscription.getCost());
        assertEquals("12826", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isEmpty());
    }

    @Test
    void POST_callback_with_stream_online_challenge_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(ChallengeReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "webhook_callback_verification")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.online")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-challenge-stream-online.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveChallengeReceived(captor.capture());

        final var challengeReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", challengeReceived.getMessageId());
        assertEquals(0, challengeReceived.getMessageRetry());
        assertEquals("webhook_callback_verification", challengeReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", challengeReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", challengeReceived.getMessageTimestamp());
        assertEquals("stream.online", challengeReceived.getSubscriptionType());
        assertEquals(1, challengeReceived.getSubscriptionVersion());

        final var callback = challengeReceived.getCallback();

        assertTrue(callback.getChallenge().isPresent());
        assertEquals("pogchamp-kappa-360noscope-vohiyo", callback.getChallenge().get());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("webhook_callback_verification_pending", subscription.getStatus());
        assertEquals("stream.online", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(1, subscription.getCost());
        assertEquals("12826", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isEmpty());
    }

    @Test
    void POST_callback_with_stream_offline_challenge_should_invoke_service_with_expected_notification() throws Exception {
        final var captor = ArgumentCaptor.forClass(ChallengeReceivedV1.class);

        mockMvc.perform(post("/callback")
                        .header("Twitch-Eventsub-Message-Id", "befa7b53-d79d-478f-86b9-120f112b044e")
                        .header("Twitch-Eventsub-Message-Retry", 0)
                        .header("Twitch-Eventsub-Message-Type", "webhook_callback_verification")
                        .header("Twitch-Eventsub-Message-Signature", "sha256=d66824350041dce130e3478f5a7")
                        .header("Twitch-Eventsub-Message-Timestamp", "2019-11-16T10:11:12.123Z")
                        .header("Twitch-Eventsub-Subscription-Type", "stream.offline")
                        .header("Twitch-Eventsub-Subscription-Version", 1)
                        .content(fileAsString("/json/callback/POST-challenge-stream-offline.json")).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(callbackServiceMock, times(1)).saveChallengeReceived(captor.capture());

        final var challengeReceived = captor.getValue();
        assertEquals("befa7b53-d79d-478f-86b9-120f112b044e", challengeReceived.getMessageId());
        assertEquals(0, challengeReceived.getMessageRetry());
        assertEquals("webhook_callback_verification", challengeReceived.getMessageType());
        assertEquals("sha256=d66824350041dce130e3478f5a7", challengeReceived.getMessageSignature());
        assertEquals("2019-11-16T10:11:12.123Z", challengeReceived.getMessageTimestamp());
        assertEquals("stream.offline", challengeReceived.getSubscriptionType());
        assertEquals(1, challengeReceived.getSubscriptionVersion());

        final var callback = challengeReceived.getCallback();

        assertTrue(callback.getChallenge().isPresent());
        assertEquals("pogchamp-kappa-360noscope-vohiyo", callback.getChallenge().get());

        final var subscription = callback.getSubscription();
        assertEquals("f1c2a387-161a-49f9-a165-0f21d7a4e1c4", subscription.getId());
        assertEquals("webhook_callback_verification_pending", subscription.getStatus());
        assertEquals("stream.offline", subscription.getType());
        assertEquals("1", subscription.getVersion());
        assertEquals(1, subscription.getCost());
        assertEquals("12826", subscription.getCondition().get("broadcaster_user_id"));

        final var transport = subscription.getTransport();
        assertEquals("webhook", transport.getMethod());
        assertEquals("https://example.com/webhooks/callback", transport.getCallback());

        assertEquals("2019-11-16T10:11:12.123Z", subscription.getCreatedAt());

        assertTrue(callback.getEvent().isEmpty());
    }
}
