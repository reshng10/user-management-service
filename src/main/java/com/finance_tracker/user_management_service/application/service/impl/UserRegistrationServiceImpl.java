package com.finance_tracker.user_management_service.application.service.impl;

import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.application.service.UserRegistrationService;
import com.finance_tracker.user_management_service.domain.entity.User;
import com.finance_tracker.user_management_service.domain.event.DomainEventPublisher;
import com.finance_tracker.user_management_service.domain.event.UserRegisteredEvent;
import com.finance_tracker.user_management_service.domain.exception.UserAlreadyExistsException;
import com.finance_tracker.user_management_service.domain.repository.RoleRepository;
import com.finance_tracker.user_management_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DomainEventPublisher domainEventPublisher;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO userDto) throws UserAlreadyExistsException {
      Optional<User> optionalUser = userRepository.findByEmailWithRoles(userDto.getEmail());

      if(optionalUser.isPresent()){
          throw new UserAlreadyExistsException("this user already exists!");
      }
     var userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->  new IllegalArgumentException("role do not exists"));

      User user = User.builder()
              .email(userDto.getEmail())
              .name(userDto.getEmail())
              .password(passwordEncoder.encode(userDto.getPassword()))
              .roles(userRole)
                       .build();

      User registeredUser = userRepository.save(user);

  // publishing event to maybe notification service for welcome email, for instance.
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(registeredUser, Instant.now());
        domainEventPublisher.publish(userRegisteredEvent);

    }

}
