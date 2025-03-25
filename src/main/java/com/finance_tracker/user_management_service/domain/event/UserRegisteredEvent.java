package com.finance_tracker.user_management_service.domain.event;

import com.finance_tracker.user_management_service.domain.entity.User;

import java.time.Instant;

public record UserRegisteredEvent(User user, Instant timestamp) {
}
