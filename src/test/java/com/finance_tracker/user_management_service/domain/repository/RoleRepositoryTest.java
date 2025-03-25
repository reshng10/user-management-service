package com.finance_tracker.user_management_service.domain.repository;

import com.finance_tracker.user_management_service.domain.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto= create",
        "spring.datasource.url= jdbc:h2:mem:unit_test_db",
        "spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.H2Dialect"
})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp(){
        role = Role.builder()
                .name("ROLE_USER")
                .build();
    }

    @Test
    public void givenRoleNameWhenFindByNameThenReturnAll(){
     //Given
     roleRepository.save(role);

     //When
     Optional<List<Role>> roleList = roleRepository.findByName(role.getName());

     //Then
        Assertions.assertThat(roleList.get().size()).isGreaterThan(0);
    }
}
