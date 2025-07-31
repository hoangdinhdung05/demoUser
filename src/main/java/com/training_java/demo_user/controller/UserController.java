package com.training_java.demo_user.controller;

import com.training_java.demo_user.dto.PageResponse;
import com.training_java.demo_user.dto.request.UserRequest;
import com.training_java.demo_user.dto.response.UserResponse;
import com.training_java.demo_user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequest request) {
        log.info("API create user called with username={}", request.getUsername());
        try {
            UserResponse response = userService.createUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserRequest request) {
        log.info("API update user called with id={}", userId);
        try {
            UserResponse response = userService.updateUser(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/active")
    public ResponseEntity<Object> updateUserActiveStatus(@PathVariable Long userId,
                                                         @RequestParam boolean active) {
        log.info("API soft delete (active=false) called for userId={}", userId);
        try {
            userService.deleteUser(userId, active);
            return ResponseEntity.ok(Map.of("message", "User status updated successfully"));
        } catch (Exception e) {
            log.error("Error updating user status: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("API get all users called");
        try {
            List<UserResponse> users = userService.getListUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error getting users: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("API get user by ID called for id={}", userId);
        try {
            UserResponse user = userService.getUserByUserid(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Error getting user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getPage")
    public ResponseEntity<Object> getPage(@RequestParam int page, @RequestParam int size) {
        log.info("Api get page user called");
        try {

            PageResponse<?> response = userService.getPageUsers(page, size);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error getting page with error={}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
