package com.project.application.repositories;

import com.project.application.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}
