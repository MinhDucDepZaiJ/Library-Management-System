package com.library.lms.service;

import com.library.lms.model.Role;
import com.library.lms.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Long id);
    User updateRole(Long id, Role role);
    User setEnabled(Long id, boolean enabled);
    void delete(Long id);
    User getByUsername(String username);
}
