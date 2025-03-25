package com.finance_tracker.user_management_service.application.service;

import com.finance_tracker.user_management_service.application.api.UserDTO;

public interface UserManagementService {

    void deleteUser(Integer userId);
    UserDTO updateUser(UserDTO userDTO);
}
