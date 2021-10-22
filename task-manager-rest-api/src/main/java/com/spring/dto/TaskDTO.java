package com.spring.dto;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class TaskDTO implements Serializable{

	private static final long serialVersionUID = -404286923406050417L;

	private Long taskId;

	@NotEmpty(message="Name can't be empty")
	private String name;

	@NotEmpty(message="Description must not be empty")
	private String description;

	@NotNull(message = "status must not be null")
	@PositiveOrZero(message = "status must be 0 or 1")
	private Integer status;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dueDate;
		
	@NotNull()
	@PositiveOrZero(message = "projectId must be a positive number")
	private Long projectId;
	
	private String projectTitle;
}
