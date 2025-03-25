package com.finance_tracker.user_management_service.domain.repository;

import com.finance_tracker.user_management_service.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<List<Role>> findByName(String name);
}
