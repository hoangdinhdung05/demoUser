package com.training_java.demo_user.service.impl;

import com.training_java.demo_user.dto.PageResponse;
import com.training_java.demo_user.dto.request.UserRequest;
import com.training_java.demo_user.dto.response.UserResponse;
import com.training_java.demo_user.entity.User;
import com.training_java.demo_user.repository.UserRepository;
import com.training_java.demo_user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse createUser(UserRequest request) {

        log.info("Create user with username={}", request.getUsername());
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        user = userRepository.save(user);

        log.info("Create user successfully with user id={}", user.getId());
        return convertToUser(user);
    }

    @Override
    public UserResponse updateUser(long userId, UserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        User updatedUser = userRepository.save(user);

        log.info("Update user successfully with id={}", userId);
        return convertToUser(updatedUser);
    }


    @Override
    public void deleteUser(long userId, Boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);

        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getListUsers() {
        List<User> responseList = userRepository.findAll();

        return responseList.stream()
                .map(this::convertToUser)
                .toList();
    }

    @Override
    public UserResponse getUserByUserid(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToUser(user);
    }

    @Override
    public PageResponse<?> getPageUsers(int page, int size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));

        List<UserResponse> responseList = userPage.stream()
                .map(this::convertToUser)
                .toList();

        int pageNumber = userPage.getNumber() + 1;
        int sizePage = userPage.getSize();
        long totalPage = userPage.getTotalPages();

        return new PageResponse<>(pageNumber, sizePage, totalPage, responseList);
    }

    private UserResponse convertToUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
