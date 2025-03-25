package com.finance_tracker.user_management_service.service.impl;

import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginResponse;
import com.finance_tracker.user_management_service.application.service.impl.UserAuthenticationServiceImpl;
import com.finance_tracker.user_management_service.domain.entity.User;
import com.finance_tracker.user_management_service.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class UserAuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;

    private UserLoginDTO userLoginDTO;

    @BeforeEach
    public void setUp(){
        userLoginDTO = new UserLoginDTO("email@gmail.com","plainPassword");
    }

    @Test
    public void givenUserWhenLoginThenAuthenticate(){
        //Given
        // Create a dummy UserDetails object for the authenticated user
        User user = User.builder()
                .email("test_123@gmail.com")
                .password("plainText")
                .build();

        // Simulate the authentication process:
        // When the AuthenticationManager is called with any UsernamePasswordAuthenticationToken,
        // return a token containing the dummy user details.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, userLoginDTO.password());

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(usernamePasswordAuthenticationToken);

        //Simulate jwtToken
        String dummyJwtToken = "DummyJWTSecretKey";
        given(jwtUtil.generateToken(user)).willReturn(dummyJwtToken);

        //When - calling method itself
        UserLoginResponse userLoginResponse = userAuthenticationService.login(userLoginDTO);

        //Then - verify
        Assertions.assertEquals(dummyJwtToken, userLoginResponse.getJwtToken());
    }
}
