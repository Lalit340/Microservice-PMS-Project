package com.lp.pmsProjectService.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "task")
@Getter
@Setter
public class TaskModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TID")
	private long tid;
	
	@Column(name = "Pname")
	@NotNull
	private String name;
	
	@Column(name = "Description")
	@NotNull
	private String description;
	
	@Column(name = "Status")
	private boolean status ;
	
	@ManyToMany(mappedBy = "taskModel")
	private List<ProjectModel> project ;

	private long userId;
}
