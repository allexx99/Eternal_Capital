package com.example.user_service.controller;

import com.example.user_service.dto.AuthRequestDTO;
import com.example.user_service.dto.AuthResponseDTO;
import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.User;
import com.example.user_service.service.impl.AuthenticationService;
import com.example.user_service.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping(value = "/helloWorld")
    public ResponseEntity<String> Hello() {
        return ResponseEntity.ok("Hello, World, from secured endpoint!");
    }

    // REGISTER
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authenticationService.register(userDTO));
    }

    // AUTHENTICATE
    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO dto = authenticationService.authenticate(request);
        return ResponseEntity.ok(dto);
        /*if(dto != null)
            return ResponseEntity.ok(dto);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);*/
    }

    // add user
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping(value = "/addUser")
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO, @NonNull HttpServletRequest request) {
        String jwt = (request.getHeader("Authorization")).substring(7);
        User user = userService.addUser(userDTO, jwt);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // read users
    @GetMapping(value = "/getUsers")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // update user
    @PutMapping(value = "/updateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, @PathVariable long id, @NonNull HttpServletRequest request) {
        String jwt = (request.getHeader("Authorization")).substring(7);
        userService.updateUser(id,userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete user
    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id, @NonNull HttpServletRequest request) {
        String jwt = (request.getHeader("Authorization")).substring(7);
        userService.deleteUser(id, jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getUsersById/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable long id) {
        User user = userService.getUsersById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
