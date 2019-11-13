package com.lp.pmsProjectService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lp.pmsProjectService.model.ProjectModel;


public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {

	 public List<ProjectModel> findByUserId(Long userId); 
}
