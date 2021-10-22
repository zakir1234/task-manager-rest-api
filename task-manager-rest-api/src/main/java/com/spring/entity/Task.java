package com.spring.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="TASK")
@EntityListeners(AuditingEntityListener.class)
public class Task extends Auditable implements Serializable{

	private static final long serialVersionUID = 7070077360009757994L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="TASK_ID")
	private Long taskId;
	
	@Column(name="NAME", nullable = false)
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;
	
	@Column(name="STATUS", columnDefinition="INT(1) NOT NULL COMMENT '1 for open, 2 for in progress, 3 for closed'")
	private Integer status;
	
	@Column(name = "DUE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID", nullable = false)	
	private Project project;
		

}
