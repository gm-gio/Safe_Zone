package com.george.user.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicConfig {

    @Value("${spring.kafka.partitions}")
    private Integer partitions;

    @Value("${spring.kafka.topics.user-register}")
    private String userRegisterTopic;

    @Bean
    NewTopic createTopic(){
        return TopicBuilder.name(userRegisterTopic)
                .partitions(partitions)
                .build();
    }
}
