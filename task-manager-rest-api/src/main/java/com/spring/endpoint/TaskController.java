package com.spring.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.common.enums.MessageContainer;
import com.spring.dto.TaskDTO;
import com.spring.service.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {
	@Autowired
	private TaskService taskService;

		
	@PostMapping("/save")
	public ResponseEntity<?> saveTask(@RequestBody @Valid TaskDTO taskDTO) {		
		taskService.saveOrUpdateTask(taskDTO);		
		return new ResponseEntity<String>(MessageContainer.MESSAGE_SAVE_SUCCESS.getMessageDetails(), HttpStatus.CREATED);
	}
	
	@PutMapping(path="/update")
	public ResponseEntity<?> updateTask(@RequestBody @Valid TaskDTO taskDTO) {		
		taskService.saveOrUpdateTask(taskDTO);		
		return new ResponseEntity<String>(MessageContainer.MESSAGE_UPDATE_SUCCESS.getMessageDetails(), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<TaskDTO>> saveTask() {	
		return new ResponseEntity<List<TaskDTO>>(taskService.findAllTasks(), HttpStatus.FOUND);		
	}
	
	@GetMapping("/find-all/{projectId}")
	public ResponseEntity<List<TaskDTO>> getAllTaskBy(@PathVariable Long projectId) {	
		return new ResponseEntity<List<TaskDTO>>(taskService.getAllTaskBy(projectId), HttpStatus.FOUND);		
	}
	
	@GetMapping("/find/by/{user}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<TaskDTO>> getAllTaskByUser(@PathVariable String user) {	
		return new ResponseEntity<List<TaskDTO>>(taskService.getAllTaskByUser(user), HttpStatus.FOUND);		
	}
	
	@GetMapping("/find-all/expired-tasks")
	public ResponseEntity<List<TaskDTO>> getAllExpiredTasks() {	
		return new ResponseEntity<List<TaskDTO>>(taskService.getAllExpiredTask(), HttpStatus.FOUND);		
	}
	
	@GetMapping("/find-all/by")
	public ResponseEntity<List<TaskDTO>> getAllTaskByStatus(@RequestParam Integer status) {	
		return new ResponseEntity<List<TaskDTO>>(taskService.getAllTaskByStatus(status), HttpStatus.FOUND);		
	}
	
	
	
	@GetMapping("/find/{taskId}")
	public ResponseEntity<TaskDTO> editTask(@PathVariable Long taskId) {	 
		return new ResponseEntity<TaskDTO>(taskService.findByTaskID(taskId), HttpStatus.FOUND);
	}
	
	@DeleteMapping("/delete/{taskId}")
	public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {		
		taskService.deleteTask(taskId);
		return new ResponseEntity<String>(MessageContainer.MESSAGE_DELETE_SUCCESS.getMessageDetails(), HttpStatus.OK);		
	}
}
