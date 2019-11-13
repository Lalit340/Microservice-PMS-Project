package com.lp.pmsProjectService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp.pmsProjectService.model.TaskModel;


public interface TaskRepository extends JpaRepository<TaskModel, Long> {

	
	public List<TaskModel> findByUserId(long userId);
}
