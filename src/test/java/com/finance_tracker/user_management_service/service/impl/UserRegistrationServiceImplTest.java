package com.finance_tracker.user_management_service.service.impl;

import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.application.service.UserRegistrationService;
import com.finance_tracker.user_management_service.application.service.impl.UserRegistrationServiceImpl;
import com.finance_tracker.user_management_service.domain.entity.Role;
import com.finance_tracker.user_management_service.domain.entity.User;
import com.finance_tracker.user_management_service.domain.event.DomainEventPublisher;
import com.finance_tracker.user_management_service.domain.exception.UserAlreadyExistsException;
import com.finance_tracker.user_management_service.domain.repository.RoleRepository;
import com.finance_tracker.user_management_service.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;


    private UserDTO userDTO;

    private User user;

    private List<Role> roleList;

    @BeforeEach
    public void setUp(){
      roleList = new ArrayList<>();

        userDTO = UserDTO.builder()
                .name("John").email("john123@gmail.com").password("plaintext").build();

        Role role = Role.builder().name("ROLE_USER").build();
        roleList.add(role);

      user = User.builder()
              .name(userDTO.getName())
              .email(userDTO.getEmail())
              .password(userDTO.getPassword())
              .roles(roleList)
              .build();
    }

    @Test
    public void givenUserWhenRegisterThenRegisterUser() throws UserAlreadyExistsException {

        //Given
        given(userRepository.findByEmailWithRoles(userDTO.getEmail())).willReturn(Optional.empty());
        given(roleRepository.findByName("ROLE_USER")).willReturn(Optional.of(roleList));
        given(passwordEncoder.encode(userDTO.getPassword())).willReturn("encodedPassword");

        User savedUser = User.builder()
                .name(userDTO.getName())
                .password("encodedPassword")
                .email(userDTO.getEmail())
                .roles(roleList)
                .build();

        given(userRepository.save(any(User.class))).willReturn(savedUser); // cant pass user in save method since passed object and stubbed objects must be identical otherwise throws error

        //When
        userRegistrationService.registerUser(userDTO);

        //Then - verify assertions
        Assertions.assertThat(savedUser).isNotNull();
        verify(domainEventPublisher, times(1)).publish(any());
    }

    @Test
    public void givenUserWhenRegisterThenThrowsException()  {
        //Given
        given(userRepository.findByEmailWithRoles(userDTO.getEmail())).willReturn(Optional.of(user));

        // When - calling method or behaviour itself inside assertThrows
        org.junit.jupiter.api.Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userRegistrationService.registerUser(userDTO));

        //Then - verify assertions
        verify(userRepository,never()).save(user);
        verify(domainEventPublisher,never()).publish(any());
    }

}
