package com.finance_tracker.user_management_service.presentation.controller;

import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginResponse;
import com.finance_tracker.user_management_service.application.service.UserAuthenticationService;
import com.finance_tracker.user_management_service.application.service.UserRegistrationService;
import com.finance_tracker.user_management_service.domain.exception.UserAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Summarization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {

    private final UserAuthenticationService userAuthenticationService;
    private final UserRegistrationService userRegistrationService;

    @Operation(summary = "registers user to system", description = "register a new user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody UserDTO userDTO) throws UserAlreadyExistsException {
       userRegistrationService.registerUser(userDTO);
    }

    @Operation(summary = "user logins to system", description = "authenticates user and returns JWT token")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
   public UserLoginResponse login(@Valid @RequestBody UserLoginDTO userLoginDTO){
       return userAuthenticationService.login(userLoginDTO);
    }
}
