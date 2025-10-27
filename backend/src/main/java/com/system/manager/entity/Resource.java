package com.system.manager.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "sys_resource")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String resourceName;
    private String resourceKey;
    private String resourceType; // menu, button, api
    private String url;
    private String method; // GET, POST, PUT, DELETE
    private Long parentId;
    private Integer sort;
    private String icon;
    private Integer status; // 0: 禁用, 1: 启用
    
    @ManyToMany(mappedBy = "resources")
    private List<Role> roles;
}