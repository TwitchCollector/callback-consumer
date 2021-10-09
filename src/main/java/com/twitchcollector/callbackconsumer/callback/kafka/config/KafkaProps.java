package com.twitchcollector.callbackconsumer.callback.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("kafka")
public class KafkaProps {

    /**
     * Name of the callback topic
     */
    private String callbackTopicName;

    /**
     * Amount of partitions for the callback topic
     */
    private int callbackTopicPartitions;

    /**
     * Replication factor for the callback topic
     */
    private short callbackTopicReplicas;

    public String getCallbackTopicName() {
        return callbackTopicName;
    }

    public void setCallbackTopicName(String callbackTopicName) {
        this.callbackTopicName = callbackTopicName;
    }

    public int getCallbackTopicPartitions() {
        return callbackTopicPartitions;
    }

    public void setCallbackTopicPartitions(int callbackTopicPartitions) {
        this.callbackTopicPartitions = callbackTopicPartitions;
    }

    public short getCallbackTopicReplicas() {
        return callbackTopicReplicas;
    }

    public void setCallbackTopicReplicas(short callbackTopicReplicas) {
        this.callbackTopicReplicas = callbackTopicReplicas;
    }
}
