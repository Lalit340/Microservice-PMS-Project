package com.lp.pmsProjectService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lp.pmsProjectService.dto.TaskDto;
import com.lp.pmsProjectService.exception.InternalException;
import com.lp.pmsProjectService.model.ProjectModel;
import com.lp.pmsProjectService.model.TaskModel;
import com.lp.pmsProjectService.repository.ProjectRepository;
import com.lp.pmsProjectService.repository.TaskRepository;
import com.lp.pmsProjectService.respone.Response;
import com.lp.pmsProjectService.utility.ResponseHelper;
import com.lp.pmsProjectService.utility.UserTokenGenerate;

@Service("taskService")
public class TaskServiceImplementation implements TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProjectRepository projRepository;

	@Autowired
	private UserTokenGenerate userTokenGenerate;

	@Override
	public Response createTask(TaskDto dto) {

		TaskModel model = modelMapper.map(dto, TaskModel.class);
		TaskModel responseModel = taskRepository.save(model);
		if (responseModel == null) {
			throw new InternalException("Task has been not created because of Internal Problem", 500);
		}

		Response response = ResponseHelper.responseSender("Succefully Task created", 200);
		return response;
	}

	@Override
	public List<TaskModel> getData() {
		List<TaskModel> model = taskRepository.findAll();
		return model;
	}

	@Override
	public Response asignTask(long pid, long tid) {
		Optional<ProjectModel> projectModel = projRepository.findById(pid);
		if (!projectModel.isPresent())
			throw new InternalException("No project  is present in the pid ", 400);
		Optional<TaskModel> taskModel = taskRepository.findById(tid);
		if (!taskModel.isPresent())
			throw new InternalException("No Task  is present in the tid ", 400);
		if (!projectModel.get().getTaskModel().contains(taskModel.get())
				&& !taskModel.get().getProject().contains(projectModel.get())) {
			projectModel.get().getTaskModel().add(taskModel.get());
			taskModel.get().getProject().add(projectModel.get());
			projRepository.save(projectModel.get());
			taskRepository.save(taskModel.get());
			Response response = ResponseHelper.responseSender("Succefully Task asign to project ", 200);
			return response;
		} else
			throw new InternalException("Task is already present ", 400);
	}

	@Override
	public Response asignTaskUser(String token, long tid) {
		long userId = userTokenGenerate.varifyToken(token);
		if (userId == 0)
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());

		Optional<TaskModel> taskModel = taskRepository.findById(tid);
		if (!taskModel.isPresent())
			throw new InternalException("No Task  is present in the tid ", HttpStatus.EXPECTATION_FAILED.value());

		if (taskModel.get().getUserId() != userId) {
			taskModel.get().setUserId(userId);
			taskRepository.save(taskModel.get());
			Response response = ResponseHelper.responseSender("Succefully Task asign to user ", HttpStatus.OK.value());
			return response;
		} else
			throw new InternalException("Task is already present ", HttpStatus.EXPECTATION_FAILED.value());
	}

	@Override
	public List<TaskModel> getListTask(long pid) {
		Optional<ProjectModel> projectModel = projRepository.findById(pid);
		if (projectModel.isPresent()) {
			List<TaskModel> taskModel = projectModel.get().getTaskModel();
			System.out.println(" tasks :" + taskModel);
			return taskModel;
		} else
			throw new InternalException("In this pid  project is not present ", 400);
	}

	@Override
	public Response changeStatus(long tid) {
		Optional<TaskModel> taskModel = taskRepository.findById(tid);
		if (taskModel.isPresent()) {
			if (taskModel.get().isStatus()) {
				taskModel.get().setStatus(false);
				taskRepository.save(taskModel.get());
				System.out.println("status " + taskModel.get().isStatus());
				Response response = ResponseHelper.responseSender("Succefully Change the status ", 200);
				return response;

			} else {
				taskModel.get().setStatus(true);
				taskRepository.save(taskModel.get());
				System.out.println("status " + taskModel.get().isStatus());
				Response response = ResponseHelper.responseSender("Succefully hange the status ", 200);
				return response;

			}
		} else
			throw new InternalException("In this tid  Task is not present ", 400);
	}

	@Override
	public Response delete(long id) {
		taskRepository.deleteById(id);
		Response response = ResponseHelper.responseSender("Succefully Task Delete", 200);
		return response;
	}

	@Override
	public Response remove(long pid, long tid) {
		Optional<ProjectModel> model = projRepository.findById(pid);
		if (!model.isPresent()) {
			throw new InternalException("No Projects are present ", 401);
		}
		Optional<TaskModel> taskModel = taskRepository.findById(tid);
		if (!taskModel.isPresent()) {
			throw new InternalException("No UserData are present ", 402);
		}
		if (model.get().getTaskModel().remove(taskModel.get())) {
			if (taskModel.get().getProject().remove(model.get())) {
				taskRepository.save(taskModel.get());
				Response response = ResponseHelper.responseSender("Succefully user Deleted", 200);
				return response;
			} else {
				Response response = ResponseHelper.responseSender("user not Deleted from project ", 403);
				return response;
			}
		} else {
			Response response = ResponseHelper.responseSender("user not Deleted from project ", 403);
			return response;
		}
	}

	@Override
	public Response update(TaskDto dto, long id) {
		Optional<TaskModel> model = taskRepository.findById(id);
		if (!model.isPresent()) {
			throw new InternalException("Task data Not Updated ", 401);
		}
		model.get().setDescription(dto.getDescription());
		model.get().setName(dto.getName());
		TaskModel responseModel = taskRepository.save(model.get());
		if (responseModel == null) {
			throw new InternalException("Task data Not Updated because Of Some Internal problem", 402);
		}
		Response response = ResponseHelper.responseSender("Succefully Task Updated", 200);
		return response;
	}

	@Override
	public List<TaskModel> getTask(String token) {
		long userId = userTokenGenerate.varifyToken(token);
		if (userId == 0)
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());

		List<TaskModel> model = taskRepository.findByUserId(userId);
		System.out.println(" task :" + model);
		return model;
	}

}
