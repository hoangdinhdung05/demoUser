package com.training_java.demo_user.service;

import com.training_java.demo_user.dto.PageResponse;
import com.training_java.demo_user.dto.request.UserRequest;
import com.training_java.demo_user.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse updateUser(long userId, UserRequest request);

    void deleteUser(long userId, Boolean active);

    List<UserResponse> getListUsers();

    UserResponse getUserByUserid(long userId);

    PageResponse<?> getPageUsers(int page, int size);
}
