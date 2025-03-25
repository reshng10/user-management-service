package com.finance_tracker.user_management_service.application.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserLoginResponse {

    private String jwtToken;
}
