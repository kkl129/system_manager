package com.system.manager.service;

import com.system.manager.entity.User;
import java.util.List;

public interface UserService {
    User save(User user);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsByUsername(String username);
    User update(User user);
    User updateRoles(Long userId, List<Long> roleIds);
}