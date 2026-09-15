package com.library.lms.service.impl;

import com.library.lms.exception.ResourceNotFoundException;
import com.library.lms.model.Role;
import com.library.lms.model.User;
import com.library.lms.repository.UserRepository;
import com.library.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id: " + id));
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng: " + username));
    }

    @Override
    @Transactional
    public User updateRole(Long id, Role role) {
        User user = getById(id);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User setEnabled(Long id, boolean enabled) {
        User user = getById(id);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = getById(id);
        userRepository.delete(user);
    }
}
