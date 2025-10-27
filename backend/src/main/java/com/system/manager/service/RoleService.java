package com.system.manager.service;

import com.system.manager.entity.Role;
import java.util.List;

public interface RoleService {
    Role save(Role role);
    Role findById(Long id);
    List<Role> findAll();
    void deleteById(Long id);
    boolean existsByRoleName(String roleName);
    Role update(Role role);
    Role updateResources(Long roleId, List<Long> resourceIds);
}