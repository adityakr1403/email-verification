package com.example.userservice.service;

import com.example.userservice.domain.User;

public interface UserService {
    User saveUser(User user);
    Boolean verifyToken(String token);
}
