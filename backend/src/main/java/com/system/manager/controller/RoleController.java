package com.system.manager.controller;

import com.system.manager.entity.Role;
import com.system.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<?> addRole(@RequestBody Role role) {
        if (roleService.existsByRoleName(role.getRoleName())) {
            return ResponseEntity.badRequest().body("角色名已存在");
        }
        Role savedRole = roleService.save(role);
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Role>> getRoleList() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }

    @PutMapping("/update")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role updatedRole = roleService.update(role);
        if (updatedRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.ok("删除成功");
    }

    @PutMapping("/{id}/resources")
    public ResponseEntity<Role> updateRoleResources(@PathVariable Long id, @RequestBody List<Long> resourceIds) {
        Role updatedRole = roleService.updateResources(id, resourceIds);
        if (updatedRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/check-rolename/{roleName}")
    public ResponseEntity<Map<String, Boolean>> checkRoleName(@PathVariable String roleName) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", roleService.existsByRoleName(roleName));
        return ResponseEntity.ok(result);
    }
}