//package com.example.user_service.service;
//
//import com.example.user_service.dto.UserDTO;
//import com.example.user_service.model.User;
//import com.example.user_service.model.UserRole;
//import com.example.user_service.repository.UserRepo;
//import com.example.user_service.service.impl.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//public class UserServiceTest {
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
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testAddUser() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setPassword("password");
//
//        User user = new User();
//        user.setPassword("encodedPassword");
//
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(userRepo.save(any(User.class))).thenReturn(user);
//
//        User savedUser = userService.addUser(userDTO, "jwtToken");
//
//        assertNotNull(savedUser);
//        assertEquals("encodedPassword", savedUser.getPassword());
//        verify(userRepo, times(1)).save(any(User.class));
//        // Add more assertions and verifications for webClient interaction if needed
//    }
//
//    @Test
//    void testGetUsers() {
//        List<User> users = List.of(new User(), new User());
//        when(userRepo.findAll()).thenReturn(users);
//
//        List<User> retrievedUsers = userService.getUsers();
//
//        assertEquals(2, retrievedUsers.size());
//        verify(userRepo, times(1)).findAll();
//    }
//
//    @Test
//    void testUpdateUser() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("username");
//        userDTO.setPassword("password");
//        userDTO.setFirstName("firstName");
//        userDTO.setLastName("lastName");
//        userDTO.setEmail("email@example.com");
//        userDTO.setRole(UserRole.INVESTOR);
//
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepo.findUsersById(anyLong())).thenReturn(user);
//
//        userService.updateUser(1L, userDTO);
//
//        verify(userRepo, times(1)).findUsersById(anyLong());
//        verify(userRepo, times(1)).save(any(User.class));
//        // Add more assertions if needed
//    }
//
//    @Test
//    void testDeleteUser() {
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepo.findUsersById(anyLong())).thenReturn(user);
//
//        userService.deleteUser(1L, "jwtToken");
//
//        verify(userRepo, times(1)).findUsersById(anyLong());
//        verify(userRepo, times(1)).delete(any(User.class));
//        // Add more assertions and verifications for webClient interaction if needed
//    }
//
//    @Test
//    void testGetUsersById() {
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepo.findUsersById(anyLong())).thenReturn(user);
//
//        User retrievedUser = userService.getUsersById(1L);
//
//        assertNotNull(retrievedUser);
//        assertEquals(1L, retrievedUser.getId());
//        verify(userRepo, times(1)).findUsersById(anyLong());
//    }
//
//    @Test
//    void testFindUserByUsername() {
//        User user = new User();
//        user.setUsername("username");
//
//        when(userRepo.findByUsername(anyString())).thenReturn(user);
//
//        UserDTO userDTO = userService.findUserByUsername("username");
//
//        assertNotNull(userDTO);
//        assertEquals("username", userDTO.getUsername());
//        verify(userRepo, times(1)).findByUsername(anyString());
//    }
//}
