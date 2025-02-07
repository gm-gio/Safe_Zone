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

    @Value("${spring.kafka.topics.user-remove}")
    private String userRemoveTopic;

    @Value("${spring.kafka.topics.user-update}")
    private String userUpdateTopic;


    @Bean
    NewTopic userRegisterTopic(){
        return TopicBuilder.name(userRegisterTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    NewTopic userRemoveTopic(){
        return TopicBuilder.name(userRemoveTopic)
                .partitions(partitions)
                .build();
    }

    @Bean
    NewTopic userUpdateTopic(){
        return TopicBuilder.name(userUpdateTopic)
                .partitions(partitions)
                .build();
    }
}
