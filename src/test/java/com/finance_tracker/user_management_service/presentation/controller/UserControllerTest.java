package com.finance_tracker.user_management_service.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginResponse;
import com.finance_tracker.user_management_service.application.service.UserAuthenticationService;
import com.finance_tracker.user_management_service.application.service.UserRegistrationService;
import com.finance_tracker.user_management_service.domain.repository.RoleRepository;
import com.finance_tracker.user_management_service.infrastructure.security.JwtAuthFilter;
import com.finance_tracker.user_management_service.infrastructure.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class))
@AutoConfigureMockMvc(addFilters = false) // Disables security filters otherwise security related 403 NOT_ALLOWED emerge
@ActiveProfiles("test") // enabling application-test.yml otherwise asks for RoleRepository, Runner bean.
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserAuthenticationService userAuthenticationService;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @Autowired
    private ObjectMapper objectMapper;

   @Test
    public void givenValidUserDTO_whenUserRegister_thenStatus201Created() throws Exception {
       // Given
       UserDTO userDTO = new UserDTO("test","email123@gmail.com","encodedPassword");

       // When & Then
       mockMvc.perform(post("/api/user/register")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated());
   }

   @Test
   public void givenValidUserLoginDTO_whenLogin_thenReturnsStatusOkAndToken() throws Exception {
       // Given
       UserLoginDTO userLoginDTO = new UserLoginDTO("username", "password");
       UserLoginResponse response = new UserLoginResponse("token");

       Mockito.when(userAuthenticationService.login(Mockito.any(UserLoginDTO.class)))
               .thenReturn(response);

       mockMvc.perform(post("/api/user/login")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(userLoginDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jwtToken").value("token"));

   }
}