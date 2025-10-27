package com.system.manager.dao;

import com.system.manager.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    boolean existsByResourceKey(String resourceKey);
}