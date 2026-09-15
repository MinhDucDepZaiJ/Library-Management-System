package com.library.lms.service;

import com.library.lms.dto.request.LoginRequest;
import com.library.lms.dto.request.RegisterRequest;
import com.library.lms.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}
