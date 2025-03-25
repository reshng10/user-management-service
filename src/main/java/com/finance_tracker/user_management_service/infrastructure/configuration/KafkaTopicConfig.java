package com.finance_tracker.user_management_service.infrastructure.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userEventsTopic(){
        return new NewTopic("user-events",1, (short) 1);
    }
}
