package com.finance_tracker.user_management_service.application.service.impl;

import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginResponse;
import com.finance_tracker.user_management_service.application.service.UserAuthenticationService;
import com.finance_tracker.user_management_service.domain.entity.User;
import com.finance_tracker.user_management_service.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponse login(UserLoginDTO userLoginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userLoginDTO.email(),userLoginDTO.password());
         Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
          User user = (User) auth.getPrincipal();
          String jwtToken = jwtUtil.generateToken(user);
       return UserLoginResponse.builder()
               .jwtToken(jwtToken)
               .build();
    }
}
