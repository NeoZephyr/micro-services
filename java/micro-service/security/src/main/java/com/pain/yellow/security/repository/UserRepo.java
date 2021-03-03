package com.pain.yellow.security.repository;

import com.pain.yellow.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    long countByUsername(String username);

    long countByEmail(String email);

    long countByMobile(String mobile);
}
