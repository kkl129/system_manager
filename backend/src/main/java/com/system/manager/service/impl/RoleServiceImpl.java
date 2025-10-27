package com.system.manager.service.impl;

import com.system.manager.dao.RoleRepository;
import com.system.manager.dao.ResourceRepository;
import com.system.manager.entity.Role;
import com.system.manager.entity.Resource;
import com.system.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    @Transactional
    public Role update(Role role) {
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);
        if (existingRole != null) {
            existingRole.setRoleDesc(role.getRoleDesc());
            existingRole.setStatus(role.getStatus());
            return roleRepository.save(existingRole);
        }
        return null;
    }

    @Override
    @Transactional
    public Role updateResources(Long roleId, List<Long> resourceIds) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            List<Resource> resources = resourceRepository.findAllById(resourceIds);
            role.setResources(resources);
            return roleRepository.save(role);
        }
        return null;
    }
}