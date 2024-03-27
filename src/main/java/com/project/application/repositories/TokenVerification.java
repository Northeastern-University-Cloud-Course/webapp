package com.project.application.repositories;

import com.project.application.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenVerification extends JpaRepository<Token, String> {
    Optional<Token> findByLink(String link);
}
