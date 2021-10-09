package com.twitchcollector.callbackconsumer.callback.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic callbackTopic(KafkaProps props) {
        return TopicBuilder.name(props.getCallbackTopicName()).partitions(props.getCallbackTopicPartitions()).replicas(props.getCallbackTopicReplicas()).build();
    }
}
