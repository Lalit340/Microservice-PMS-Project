package com.lp.pmsProjectService.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lp.pmsProjectService.dto.TaskDto;
import com.lp.pmsProjectService.model.TaskModel;
import com.lp.pmsProjectService.respone.Response;



@Service
public interface TaskService {

	public Response createTask(TaskDto dto);
	
	public List<TaskModel> getData();
	
	public Response asignTask(long pid, long tid);
	
	public Response asignTaskUser(String token, long tid);
	
	public List<TaskModel> getListTask(long pid);
	
	public List<TaskModel> getTask(String token);

	public Response changeStatus(long tid);
	
	public Response delete(long id);
	
	public Response remove(long pid, long tid);
	
	public Response update(TaskDto dto, long id);

}
