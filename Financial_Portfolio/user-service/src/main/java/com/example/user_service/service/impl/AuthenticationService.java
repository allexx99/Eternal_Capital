package com.example.user_service.service.impl;

import com.example.user_service.dto.AuthRequestDTO;
import com.example.user_service.dto.AuthResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.Token;
import com.example.user_service.model.User;
import com.example.user_service.model.UserRole;
import com.example.user_service.repository.TokenRepo;
import com.example.user_service.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final WebClient webClient;

    @Transactional
    public AuthResponseDTO register(UserDTO userDTO) {
        User user = userDTO.convertToModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(userDTO.getRole() == null) {
            user.setRole(UserRole.INVESTOR);
        }
        var savedUser = userRepo.save(user);
//        var jwtToken = jwtService.generateToken(user);
        // Generate token with user's role
        var jwtToken = jwtService.generateToken(new HashMap<>(), user, user.getRole().name());
        saveUserToken(savedUser, jwtToken);

        if(user.getRole() == UserRole.INVESTOR) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            String response = webClient.post()
                    .uri("http://localhost:8081/addInvestor/" + user.getId())
                    .headers(httpHeaders -> httpHeaders.addAll(requestEntity.getHeaders()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        return AuthResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .token(jwtToken)
                .build();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepo.findUsersByUsername(request.getUsername())
                    .orElseThrow();
//            var jwtToken = jwtService.generateToken(user);
            // Generate token with user's role
            var jwtToken = jwtService.generateToken(new HashMap<>(), user, user.getRole().name());
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthResponseDTO.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .token(jwtToken)
                    .build();
        } catch (AuthenticationException e) {
            return null;
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var date = jwtService.extractExpiration(jwtToken);

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .date(date)
                .expired(false)
                .build();
        tokenRepo.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepo.findAllValidTokenByUser(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
        });
        tokenRepo.saveAll(validUserTokens);
    }
}
