package com.finance_tracker.user_management_service.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance_tracker.user_management_service.application.api.UserDTO;
import com.finance_tracker.user_management_service.application.api.UserLoginDTO;
import com.finance_tracker.user_management_service.domain.entity.User;
import com.finance_tracker.user_management_service.domain.repository.UserRepository;
import com.finance_tracker.user_management_service.infrastructure.event.KafkaDomainEventPublisher;
import com.netflix.discovery.EurekaClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class) // I already excluded KafkaAutoConfiguration.class
// in application-test.yml, this(using exclude attribute in @EnableAutoConfiguration ) is just another way of excluding autoconfig classes.
@ActiveProfiles("test") // we are configuring integration test related properties like H2 db, shutting down eureka connection
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

   // When a mock for your DomainEventPublisher using @MockitoBean provided,
   // you essentially override any Kafka-related configuration that was expecting a real publisher. In other words, by supplying a mock bean for the DomainEventPublisher, Spring Boot did not need to auto-configure Kafka, and so KafkaAutoConfiguration wasn’t triggered, which removed the Kafka-related errors in your integration tests.
    @MockitoBean
    private KafkaDomainEventPublisher publisher;

    @BeforeEach
    public void setUp(){
        userDTO = UserDTO.builder()
                .name("IntegrationTestName")
                .email("email123@gmail.com")
                .password("plainText")
                .build();

        userRepository.deleteAll();
    }

    @Test
   public void testUserRegistrationIntegration() throws Exception {
        //Given - this part is in setup

        //When
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());

        //Then - verify that the user is actually persisted
       User persistedUser = userRepository.findByEmailWithRoles(userDTO.getEmail()).orElse(null);
        Assertions.assertThat(persistedUser).isNotNull();

   }

   @Test
    public void testUserLoginIntegration() throws Exception {
        // Given
        // registering user first
       mockMvc.perform(post("/api/user/register")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(userDTO)))
               .andExpect(status().isCreated());

       UserLoginDTO userLoginDTO = new UserLoginDTO("email123@gmail.com","plainText");

       // When & Then
       mockMvc.perform(post("/api/user/login")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(userLoginDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath(".jwtToken").isNotEmpty());
   }
}
