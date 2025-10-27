package com.system.manager.service.impl;

import com.system.manager.dao.ResourceRepository;
import com.system.manager.dao.UserRepository;
import com.system.manager.entity.Resource;
import com.system.manager.entity.User;
import com.system.manager.entity.Role;
import com.system.manager.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        resourceRepository.deleteById(id);
    }

    @Override
    public boolean existsByResourceKey(String resourceKey) {
        return resourceRepository.existsByResourceKey(resourceKey);
    }

    @Override
    @Transactional
    public Resource update(Resource resource) {
        Resource existingResource = resourceRepository.findById(resource.getId()).orElse(null);
        if (existingResource != null) {
            existingResource.setResourceName(resource.getResourceName());
            existingResource.setUrl(resource.getUrl());
            existingResource.setMethod(resource.getMethod());
            existingResource.setParentId(resource.getParentId());
            existingResource.setSort(resource.getSort());
            existingResource.setIcon(resource.getIcon());
            existingResource.setStatus(resource.getStatus());
            return resourceRepository.save(existingResource);
        }
        return null;
    }

    @Override
    public List<Resource> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getRoles().stream()
                    .flatMap(role -> role.getResources().stream())
                    .distinct()
                    .filter(resource -> resource.getStatus() == 1)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean hasPermission(Long userId, String url, String method) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getRoles().stream()
                    .flatMap(role -> role.getResources().stream())
                    .anyMatch(resource -> 
                        resource.getUrl().equals(url) && 
                        resource.getMethod().equals(method) && 
                        resource.getStatus() == 1
                    );
        }
        return false;
    }
}