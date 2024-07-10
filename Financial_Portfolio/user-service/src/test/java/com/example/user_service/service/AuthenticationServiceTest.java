//package com.example.user_service.service;
//
//import com.example.user_service.model.User;
//import com.example.user_service.repository.UserRepo;
//import com.example.user_service.service.impl.AuthenticationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//class AuthenticationServiceTest {
//
//    @Mock
//    private UserRepo userRepo;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private WebClient webClient;
//
//    @InjectMocks
//    private AuthenticationService authenticationService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testAuthenticate() {
//        String username = "testUser";
//        String password = "testPassword";
//        String encodedPassword = "encodedPassword";
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(encodedPassword);
//
//        when(userRepo.findByUsername(anyString())).thenReturn(user);
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
//
//        User authenticatedUser = authenticationService.authenticate(username, password);
//
//        assertNotNull(authenticatedUser);
//        assertEquals(username, authenticatedUser.getUsername());
//        verify(userRepo, times(1)).findByUsername(anyString());
//        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
//    }
//
//    @Test
//    void testAuthenticateInvalidPassword() {
//        String username = "testUser";
//        String password = "testPassword";
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword("encodedPassword");
//
//        when(userRepo.findByUsername(anyString())).thenReturn(user);
//        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
//
//        User authenticatedUser = authenticationService.authenticate(username);
//
//        assertNull(authenticatedUser);
//        verify(userRepo, times(1)).findByUsername(anyString());
//        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
//    }
//
//    @Test
//    void testAuthenticateUserNotFound() {
//        String username = "testUser";
//        String password = "testPassword";
//
//        when(userRepo.findByUsername(anyString())).thenReturn(null);
//
//        User authenticatedUser = authenticationService.authenticate(username, password);
//
//        assertNull(authenticatedUser);
//        verify(userRepo, times(1)).findByUsername(anyString());
//        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
//    }
//
//    @Test
//    void testRefreshToken() {
//        String jwt = "jwtToken";
//        String newJwtToken = "newJwtToken";
//
//        // Hypothetical implementation of WebClient for refreshing token
//        // Mocking the webClient call and its response
//        when(webClient.post())
//                .thenReturn(WebClient.RequestHeadersUriSpec.class);
//
//        // Mock other interactions if needed
//
//        String refreshedToken = authenticationService.refreshToken(jwt);
//
//        assertNotNull(refreshedToken);
//        assertEquals(newJwtToken, refreshedToken);
//        // Verify webClient interactions if needed
//    }
//}
