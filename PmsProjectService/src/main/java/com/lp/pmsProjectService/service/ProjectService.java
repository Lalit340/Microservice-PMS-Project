package com.lp.pmsProjectService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lp.pmsProjectService.dto.ProjectDto;
import com.lp.pmsProjectService.model.ProjectModel;
import com.lp.pmsProjectService.respone.Response;


@Service
public interface ProjectService {

	public Response createProject(ProjectDto dto);
	
	public List<ProjectModel> getProject();

	public Response assignProject(long pid ,String token);

	public List<ProjectModel> findUserProject(String mail);
	
	public Response update(ProjectDto dto, long id);
	
	public Response delete(long id);
	
	public Response remove(long pid, String token);

	public ProjectModel findProject(long pid);

}
