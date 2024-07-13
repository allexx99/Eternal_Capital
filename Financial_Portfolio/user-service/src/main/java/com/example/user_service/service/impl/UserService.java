package com.example.user_service.service.impl;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import com.example.user_service.repository.UserRepo;
import com.example.user_service.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final WebClient webClient;


    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, WebClient webClient) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.webClient = webClient;
    }

    @Override
    @Transactional
    public User addUser(UserDTO userDTO, String jwt){
        User user = userDTO.convertToModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String response = webClient.post()
                .uri("http://localhost:8081/addInvestor/" + user.getId())
                .headers(httpHeaders -> httpHeaders.addAll(requestEntity.getHeaders()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userRepo.findAll();
        List<User> investors = new ArrayList<>();
        for (User user : users) {
            if (user.getRole() == UserRole.INVESTOR) {
                investors.add(user);
            }
        }
        return investors;
    }

    @Override
    public void updateUser(long id, UserDTO userDTO) {
        User user = userRepo.findUsersById(id);
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        userRepo.save(user);
    }

    @Override
    public void deleteUser(long id, String jwt) {
        User user = userRepo.findUsersById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String response = webClient.delete()
                .uri("http://localhost:8081/deleteInvestor/" + user.getId())
                .headers(httpHeaders -> httpHeaders.addAll(requestEntity.getHeaders()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        userRepo.delete(user);

    }

    @Override
    public User getUsersById(long id) {
        return userRepo.findUsersById(id);
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        User user = userRepo.findByUsername(username);
        return user.convertToDTO(user);
    }
}
