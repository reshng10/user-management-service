package com.finance_tracker.user_management_service.application.api;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(@NotBlank(message = "email is required.") String email,
                           @NotBlank(message = "password is required.") String password) {
}
