package com.finance_tracker.user_management_service.infrastructure.event;

import com.finance_tracker.user_management_service.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDomainEventPublisher implements DomainEventPublisher {

     private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(Object event) {
        kafkaTemplate.send("user-events",event);
    }
}
