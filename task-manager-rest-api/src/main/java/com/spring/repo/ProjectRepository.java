package com.spring.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

	List<Project> findByCreatedBy(String username); 

	
}
