package com.spring.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PROJECT")
@EntityListeners(AuditingEntityListener.class)
public class Project extends Auditable implements Serializable{
	

	private static final long serialVersionUID = 6878913904718438778L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PROJECT_ID")
	private Long projectId;
	
	@Column(name="PROJECT_TITLE", nullable = false, unique = true)
	private String projectTitle;
	
	@Column(name="DESCRIPTION",  nullable = false, unique = true)
	private String description;
	
	@Column(name="NOTE")
	private String note;
	
	@Column(name = "STATUS", columnDefinition = "Integer DEFAULT 0")
	private Integer status;
	
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
	private Set<Task> tasks = new HashSet<>();
	 

}
