package com.pain.yellow.security.repository;

import com.pain.yellow.security.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Set<Role> findByAuthorityIn(Set<String> authority);

    Optional<Role> findByAuthority(String authority);
}
