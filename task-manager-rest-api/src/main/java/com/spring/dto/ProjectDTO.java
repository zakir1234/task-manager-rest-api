package com.spring.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;

@Data
public class ProjectDTO implements Serializable{
	
	private static final long serialVersionUID = 5513891157065292938L;

	private Long projectId;

	@NotEmpty(message="Project Title must not be empty")
	private String projectTitle;

	@NotEmpty(message="Description must not be empty")
	private String description;

	@NotEmpty(message="note must not be empty")
	private String note;

	@NotNull(message = "status must not be null")
	@PositiveOrZero(message = "status must be 0 or 1 or 2")
	private Integer status;	 

}
