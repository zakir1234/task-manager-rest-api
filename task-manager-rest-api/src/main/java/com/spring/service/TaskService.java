
package com.spring.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.spring.common.enums.ErrorMessageContainer;
import com.spring.common.exception.customhandler.BadRequestExceptionHandler;
import com.spring.common.exception.customhandler.ResourceNotFoundExceptionHandler;
import com.spring.common.util.DateHelper;
import com.spring.dto.TaskDTO;
import com.spring.entity.Project;
import com.spring.entity.Task;
import com.spring.repo.TaskRepository;

/**
 * 
 * @author Zakir
 *
 */

@Service
@Transactional
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private DateHelper dateHelper;

	public void saveOrUpdateTask(TaskDTO taskDTO) {
		
		Optional.ofNullable(taskDTO).orElseThrow(() -> new BadRequestExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "taskDTO")));

		Task task = copyTaskFromDtoOpt(Optional.ofNullable(taskDTO)).get();
		
		if(taskDTO.getProjectId() != null) {
			Project project = projectService.getProjectById(taskDTO.getProjectId()).get();
			task.setProject(project);
		}else {
			throw new ResourceNotFoundExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails(), "Project ID: " + taskDTO.getProjectId()));
		}
	

		taskRepository.save(task);

	}

	
	public List<TaskDTO> findAllTasks() {

		List<Task> taskList = taskRepository.findAll();

		if (taskList.isEmpty()) {
			throw new ResourceNotFoundExceptionHandler(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails());
		}

		List<TaskDTO> taskDTOList = taskList.stream()
				.map(task -> copyTaskDtoFromEntityOpt(Optional.of(task)).get()).collect(Collectors.toList());

		return taskDTOList;
	}
	
	public List<TaskDTO> returnTaskDTOs(List<Task> tasks){
		
		if (tasks.isEmpty()) {
			throw new ResourceNotFoundExceptionHandler(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails());
		}

		List<TaskDTO> taskDTOList = tasks.stream()
				.map(task -> copyTaskDtoFromEntityOpt(Optional.of(task)).get()).collect(Collectors.toList());

		return taskDTOList;
		
	}

	public TaskDTO findByTaskID(Long taskID) {
		
		Optional<Task> taskOpt = getTaskById(taskID);
		
		return copyTaskDtoFromEntityOpt(taskOpt).get();		
	}
	
	public Optional<Task> getTaskById(Long taskId) {
		Optional<Task> taskOpt = taskRepository.findById(taskId);
		
		if(! taskOpt.isPresent()) {
			throw new ResourceNotFoundExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails(), "Task ID: " + taskId));
		} 
		
		return taskOpt;		
	}
	


	public void deleteTask(Long taskID) {
		

		Optional<Task> taskOpt = taskRepository.findById(taskID);
		
		if(! taskOpt.isPresent()) {
			throw new ResourceNotFoundExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails(), "Task ID: " + taskID));
		} 
		
		taskRepository.delete(taskOpt.get());
	}

	public Optional<TaskDTO> copyTaskDtoFromEntityOpt(Optional<Task> taskOpt) {

		if (isTaskCopiable(taskOpt)) {
		return	copyTaskDtoFromEntity(taskOpt.get());
		}

		return Optional.empty();
	}

	public Optional<Task> copyTaskFromDtoOpt(Optional<TaskDTO> taskDtoOpt) {

		if (isTaskDtoCopiable(taskDtoOpt)) {
			return copyTaskFromDTO(taskDtoOpt.get());
		}

		return Optional.empty();
	}

	private boolean isTaskCopiable(Optional<Task> taskOpt) {

		Task task = taskOpt.orElseThrow(() -> new BadRequestExceptionHandler(
				String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "task")));

		if (StringUtils.isEmpty(task.getTaskId()))
			throw new BadRequestExceptionHandler(
					String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "taskID"));

		return true;
	}

	private Optional<TaskDTO> copyTaskDtoFromEntity(Task task) {

		TaskDTO taskDTO = new TaskDTO();
		BeanUtils.copyProperties(task, taskDTO);
		
		Project project = task.getProject();
		
		if( project != null) {
			taskDTO.setProjectId(project.getProjectId());
			taskDTO.setProjectTitle(project.getProjectTitle());
		}
				
		return Optional.of(taskDTO);

	}

	private boolean isTaskDtoCopiable(Optional<TaskDTO> taskDtoOpt) {

		TaskDTO taskDTO = taskDtoOpt.orElseThrow(() -> new BadRequestExceptionHandler(
				String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "taskDTO")));

		if (StringUtils.isEmpty(taskDTO.getName()))
			throw new BadRequestExceptionHandler(
					String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "taskName"));

		return true;
	}

	private Optional<Task> copyTaskFromDTO(TaskDTO taskDto) {

		Task task = new Task();
		BeanUtils.copyProperties(taskDto, task);
		return Optional.of(task);

	}
	
	public List<TaskDTO> getAllTaskBy(Long projectId) {
		
		List<Task> projectWiseTasks = taskRepository.findByProject_ProjectId(projectId);		
		return returnTaskDTOs(projectWiseTasks);
	}
	
	public List<TaskDTO> getAllTaskByUser(String username) {		
		List<Task> projectWiseTasks = taskRepository.findByProject_CreatedBy(username);		
		return returnTaskDTOs(projectWiseTasks);
	}


	public List<TaskDTO> getAllExpiredTask() { 			
		List<Task> projectWiseTasks = taskRepository.findByDueDateBefore(dateHelper.getStartOfDay(new Date()));		
		return returnTaskDTOs(projectWiseTasks);		
	}


	public List<TaskDTO> getAllTaskByStatus(Integer status) {
		List<Task> projectWiseTasks = taskRepository.findByStatus(status);		
		return returnTaskDTOs(projectWiseTasks);
	}

}
