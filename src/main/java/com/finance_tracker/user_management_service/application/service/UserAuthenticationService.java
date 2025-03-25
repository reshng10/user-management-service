package com.finance_tracker.user_management_service.application.service;

import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginResponse;

public interface UserAuthenticationService {

    UserLoginResponse login(UserLoginDTO userLoginDTO);
}
