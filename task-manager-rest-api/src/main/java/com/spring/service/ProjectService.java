
package com.spring.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.spring.common.enums.ErrorMessageContainer;
import com.spring.common.exception.customhandler.BadRequestExceptionHandler;
import com.spring.common.exception.customhandler.ResourceNotFoundExceptionHandler;
import com.spring.dto.ProjectDTO;
import com.spring.entity.Project;
import com.spring.repo.ProjectRepository;

/**
 * 
 * @author Zakir
 *
 */

@Service
@Transactional
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public void saveOrUpdateProject(ProjectDTO projectDTO) {
		
		Optional.ofNullable(projectDTO).orElseThrow(() -> new BadRequestExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "projectDTO")));

		Project project = copyProjectFromDtoOpt(Optional.ofNullable(projectDTO)).get();

		projectRepository.save(project);

	}

	
	public List<ProjectDTO> findAllProjects() {

		List<Project> projectList = projectRepository.findAll();

		return getPrjectDtos(projectList);
	}
	
	public List<ProjectDTO> getPrjectDtos(List<Project> projectList){

		if (projectList.isEmpty()) {
			throw new ResourceNotFoundExceptionHandler(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails());
		}

		List<ProjectDTO> projectDTOList = projectList.stream()
				.map(project -> copyProjectDtoFromEntityOpt(Optional.of(project)).get()).collect(Collectors.toList());

		return projectDTOList;
	}

	public ProjectDTO findProjectDTOByID(Long projectID) {
		
		Optional<Project> projectOpt = getProjectById(projectID);
		
		return copyProjectDtoFromEntityOpt(projectOpt).get();		
	}
	
	public Optional<Project> getProjectById(Long projectId){
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		
		if(! projectOpt.isPresent()) {
			throw new ResourceNotFoundExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails(), "Project ID: " + projectId));
		} 
		
		return projectOpt;
		
	}

	public void deleteProject(Long projectID) {
		

		Optional<Project> projectOpt = projectRepository.findById(projectID);
		
		if(! projectOpt.isPresent()) {
			throw new ResourceNotFoundExceptionHandler(String.format(ErrorMessageContainer.MESSAGE_TYPE_RESOURCE_NOT_FOUND.getMessageDetails(), "Project ID: " + projectID));
		} 
		
		projectRepository.delete(projectOpt.get());
	}

	public Optional<ProjectDTO> copyProjectDtoFromEntityOpt(Optional<Project> projectOpt) {

		if (isProjectCopiable(projectOpt)) {
		return	copyProjectDtoFromEntity(projectOpt.get());
		}

		return Optional.empty();
	}

	public Optional<Project> copyProjectFromDtoOpt(Optional<ProjectDTO> projectDtoOpt) {

		if (isProjectDtoCopiable(projectDtoOpt)) {
			return copyProjectFromDTO(projectDtoOpt.get());
		}

		return Optional.empty();
	}

	private boolean isProjectCopiable(Optional<Project> projectOpt) {

		Project project = projectOpt.orElseThrow(() -> new BadRequestExceptionHandler(
				String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "project")));

		if (StringUtils.isEmpty(project.getProjectId()))
			throw new BadRequestExceptionHandler(
					String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "projectID"));

		return true;
	}

	private Optional<ProjectDTO> copyProjectDtoFromEntity(Project project) {

		ProjectDTO projectDTO = new ProjectDTO();
		BeanUtils.copyProperties(project, projectDTO);
		return Optional.of(projectDTO);

	}

	private boolean isProjectDtoCopiable(Optional<ProjectDTO> projectDtoOpt) {

		ProjectDTO projectDTO = projectDtoOpt.orElseThrow(() -> new BadRequestExceptionHandler(
				String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "projectDTO")));

		if (StringUtils.isEmpty(projectDTO.getProjectTitle()))
			throw new BadRequestExceptionHandler(
					String.format(ErrorMessageContainer.MESSAGE_TYPE_BAD_REQUEST.getMessageDetails(), "projectName"));

		return true;
	}

	private Optional<Project> copyProjectFromDTO(ProjectDTO projectDto) {

		Project project = new Project();
		BeanUtils.copyProperties(projectDto, project);
		return Optional.of(project);

	}


	public List<ProjectDTO> getAllTaskByUser(String username) {

		List<Project> projectList = projectRepository.findByCreatedBy(username);

		return getPrjectDtos(projectList);
	}

}
