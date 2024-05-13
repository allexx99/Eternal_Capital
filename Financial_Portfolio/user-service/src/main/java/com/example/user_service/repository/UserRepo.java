package com.example.user_service.repository;

import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findUsersById(long id);
    Optional<User> findUsersByUsername(String username);
    User findByRole(UserRole role);
}
