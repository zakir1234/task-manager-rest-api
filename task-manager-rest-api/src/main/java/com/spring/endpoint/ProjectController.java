package com.spring.endpoint;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.common.enums.MessageContainer;
import com.spring.dto.ProjectDTO;
import com.spring.dto.TaskDTO;
import com.spring.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;

		
	@PostMapping("/save")
	public ResponseEntity<?> saveProject(@RequestBody @Valid ProjectDTO projectDTO) {		
		projectService.saveOrUpdateProject(projectDTO);		
		return new ResponseEntity<String>(MessageContainer.MESSAGE_SAVE_SUCCESS.getMessageDetails(), HttpStatus.CREATED);
	}
	
	@PutMapping(path="/update")
	public ResponseEntity<?> updateProject(@RequestBody @Valid ProjectDTO projectDTO) {		
		projectService.saveOrUpdateProject(projectDTO);		
		return new ResponseEntity<String>(MessageContainer.MESSAGE_UPDATE_SUCCESS.getMessageDetails(), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<ProjectDTO>> saveProject() {	
		return new ResponseEntity<List<ProjectDTO>>(projectService.findAllProjects(), HttpStatus.FOUND);		
	}
	
	
	@GetMapping("/find/{projectId}")
	public ResponseEntity<ProjectDTO> editProject(@PathVariable Long projectId) {	 
		return new ResponseEntity<ProjectDTO>(projectService.findProjectDTOByID(projectId), HttpStatus.FOUND);
	}
	
	@GetMapping("/find/by/{user}")
	public ResponseEntity<List<ProjectDTO>> getAllProjectByUser(@PathVariable String user) {	
		return new ResponseEntity<List<ProjectDTO>>(projectService.getAllTaskByUser(user), HttpStatus.FOUND);		
	}
	
	@DeleteMapping("/delete/{projectId}")
	public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {		
		projectService.deleteProject(projectId);
		return new ResponseEntity<String>(MessageContainer.MESSAGE_DELETE_SUCCESS.getMessageDetails(), HttpStatus.OK);		
	}
}
