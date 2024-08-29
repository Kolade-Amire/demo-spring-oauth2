package com.kolade.demo_spring_oauth2.authentication.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);
    Optional<List<Token>> findAllByUserId(String userId);

}
