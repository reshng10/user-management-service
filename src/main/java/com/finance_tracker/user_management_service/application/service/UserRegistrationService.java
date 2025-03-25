package com.finance_tracker.user_management_service.application.service;

import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.domain.exception.UserAlreadyExistsException;

public interface UserRegistrationService {

    void registerUser(UserDTO user) throws UserAlreadyExistsException;
}
