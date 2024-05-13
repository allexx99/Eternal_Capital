package com.example.user_service.service.impl;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import com.example.user_service.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private final AuthenticationService authenticationService;

    public AdminInitializer(UserRepo userRepo, AuthenticationService authenticationService) {
        this.userRepo = userRepo;
        this.authenticationService = authenticationService;
    }
    @PostConstruct
    public void initAdmin() {
        User adminUser = userRepo.findByRole(UserRole.ADMIN);
        if(adminUser == null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("alex_n_drew");
            userDTO.setPassword("Password99");
            userDTO.setFirstName("Alex");
            userDTO.setLastName("Usuc");
            userDTO.setEmail("alexusuc@yahoo.com");
            userDTO.setRole(UserRole.ADMIN);
            // userRepo.save(adminUser);
            authenticationService.register(userDTO);
        }
    }
}
