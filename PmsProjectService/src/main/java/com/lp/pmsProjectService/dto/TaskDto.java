package com.lp.pmsProjectService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskDto {

	private String name;
	
	private String description;

	public TaskDto() {
		super();
	}
}
