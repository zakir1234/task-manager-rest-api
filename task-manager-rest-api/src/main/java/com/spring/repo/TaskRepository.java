package com.spring.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByProject_ProjectId(Long projectId);	
	List<Task> findByDueDateBefore(Date date);
	List<Task> findByStatus(Integer status);
	List<Task> findByProject_CreatedBy(String username);

}
