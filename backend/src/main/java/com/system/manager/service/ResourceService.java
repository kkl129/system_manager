package com.system.manager.service;

import com.system.manager.entity.Resource;
import java.util.List;

public interface ResourceService {
    Resource save(Resource resource);
    Resource findById(Long id);
    List<Resource> findAll();
    void deleteById(Long id);
    boolean existsByResourceKey(String resourceKey);
    Resource update(Resource resource);
    List<Resource> findByUserId(Long userId);
    boolean hasPermission(Long userId, String url, String method);
}