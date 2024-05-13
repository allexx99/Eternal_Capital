package com.example.user_service.repository;

import com.example.user_service.model.Token;
import com.example.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
    List<Token> findAllValidTokenByUser(User user);
    List<Token> findAll();
    Optional<Token> findByToken(String token);
}
