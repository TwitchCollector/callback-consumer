package com.twitchcollector.callbackconsumer.callback.kafka;

import com.twitchcollector.callbackconsumer.callback.kafka.config.KafkaProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CallbackProducer {

    private static final Logger logger = LoggerFactory.getLogger(CallbackProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public CallbackProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaProps kafkaProps) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaProps.getCallbackTopicName();
    }

    public void produce(String key, String value) {
        try {
            final var future = kafkaTemplate.send(topic, key, value);
            final var result = future.get(5, TimeUnit.SECONDS);
            final var record = result.getRecordMetadata();
            final var partition = record.partition();
            final var offset = record.offset();
            logger.info("Produced message with key: {} to partition: {} with offset: {}", key, partition, offset);
        } catch (Throwable throwable) {
            logger.error("Failed to produce message with key: {}", key);
            throw new RuntimeException(throwable);
        }
    }
}
