package com.finance_tracker.user_management_service.domain.repository;

import com.finance_tracker.user_management_service.domain.entity.Role;
import com.finance_tracker.user_management_service.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto= create-drop",
        "spring.datasource.url= jdbc:h2:mem:unit_test_db",
        "spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.H2Dialect"
})
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;

    private Role role;

    private List<Role> roleList;

    @BeforeEach
    public void setUp(){
        role = Role.builder().name("ROLE_USER").build();
        roleList = new ArrayList<>();
        roleList.add(role);

        user = User.builder().name("user").email("user@gmail.com").password("plainText")
                .roles(roleList)
                .build();
    }

    @Test
    public void givenEmailWhenFindUserWithRolesThenReturnUser(){

        //Given
        roleRepository.save(role);
        userRepository.save(user);

        //When
       Optional<User> optionalUser = userRepository.findByEmailWithRoles(user.getEmail());

       //Then
        Assertions.assertThat(optionalUser.get()).isNotNull();
        Assertions.assertThat(optionalUser.get().getRoles().size()).isEqualTo(1);

    }

}
