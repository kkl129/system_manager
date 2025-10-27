package com.system.manager.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String roleName;
    
    private String roleDesc;
    private Integer status; // 0: 禁用, 1: 启用
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_role_resource",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "resource_id")
    )
    private List<Resource> resources;
    
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}