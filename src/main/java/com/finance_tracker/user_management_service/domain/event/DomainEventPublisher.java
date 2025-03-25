package com.finance_tracker.user_management_service.domain.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
