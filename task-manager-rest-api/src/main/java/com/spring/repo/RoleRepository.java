package com.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
