package com.system.manager.controller;

import com.system.manager.entity.Resource;
import com.system.manager.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("/add")
    public ResponseEntity<?> addResource(@RequestBody Resource resource) {
        if (resourceService.existsByResourceKey(resource.getResourceKey())) {
            return ResponseEntity.badRequest().body("资源标识已存在");
        }
        Resource savedResource = resourceService.save(resource);
        return ResponseEntity.ok(savedResource);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Resource>> getResourceList() {
        List<Resource> resources = resourceService.findAll();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        Resource resource = resourceService.findById(id);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/update")
    public ResponseEntity<Resource> updateResource(@RequestBody Resource resource) {
        Resource updatedResource = resourceService.update(resource);
        if (updatedResource == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(@PathVariable Long id) {
        resourceService.deleteById(id);
        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resource>> getResourcesByUserId(@PathVariable Long userId) {
        List<Resource> resources = resourceService.findByUserId(userId);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/check-permission")
    public ResponseEntity<Map<String, Boolean>> checkPermission(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        String url = request.get("url").toString();
        String method = request.get("method").toString();
        
        boolean hasPermission = resourceService.hasPermission(userId, url, method);
        Map<String, Boolean> result = new HashMap<>();
        result.put("hasPermission", hasPermission);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check-resourcekey/{resourceKey}")
    public ResponseEntity<Map<String, Boolean>> checkResourceKey(@PathVariable String resourceKey) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", resourceService.existsByResourceKey(resourceKey));
        return ResponseEntity.ok(result);
    }
}