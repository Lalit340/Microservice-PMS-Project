package com.lp.pmsProjectService.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.lp.pmsProjectService.dto.ProjectDto;
import com.lp.pmsProjectService.exception.InternalException;
import com.lp.pmsProjectService.model.ProjectModel;
import com.lp.pmsProjectService.repository.ProjectRepository;
import com.lp.pmsProjectService.respone.Response;
import com.lp.pmsProjectService.utility.ResponseHelper;
import com.lp.pmsProjectService.utility.UserTokenGenerate;

@Service("pmsProjectService")
public class ProjectServiceImplementation implements ProjectService {

	@Autowired
	private ProjectRepository projRepository;

	@Autowired
	private UserTokenGenerate userTokenGenerate;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Response createProject(ProjectDto dto) {
		ProjectModel model = mapper.map(dto, ProjectModel.class);
		ProjectModel responseModel = projRepository.save(model);
		Response response = null;
		if (responseModel == null) {
			throw new InternalException("project add is failed", 401);
		}
		response = ResponseHelper.responseSender("Project is Created Successfully", 200);
		return response;
	}

	@Override
	public List<ProjectModel> getProject() {
		List<ProjectModel> projects = projRepository.findAll();
		if (projects != null)
			return projects;
		else
			throw new InternalException("project data is not found", 401);
	}

	@Override
	public Response assignProject(long pid, String token) {
		Response response = null;
		Optional<ProjectModel> projModel = projRepository.findById(pid);
		long userId = 0;
		if (!projModel.isPresent())
			throw new InternalException("project data is not found", 401);

		userId = userTokenGenerate.varifyToken(token);
		if (userId == 0)
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());

		projModel.get().setUserId(userId);
		projRepository.save(projModel.get());
		response = ResponseHelper.responseSender("Successfully asigned project", HttpStatus.OK.value());
		return response;

	}

	@Override
	public List<ProjectModel> findUserProject(String token) {
		long userId = userTokenGenerate.varifyToken(token);
		if (userId == 0)
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());

		List<ProjectModel> projModel = projRepository.findByUserId(userId);
		System.out.println("hi ::" + projModel);
		return projModel;
	}

	@Override
	public Response update(ProjectDto dto, long id) {
		Optional<ProjectModel> model = projRepository.findById(id);
		if (!model.isPresent()) {
			throw new InternalException("Project data Not Updated ", 401);
		}
		model.get().setDescription(dto.getDescription());
		model.get().setName(dto.getName());
		ProjectModel responseModel = projRepository.save(model.get());
		if (responseModel == null) {
			throw new InternalException("Project data Not Updated because Of Some Internal problem", 402);
		}
		Response response = ResponseHelper.responseSender("Succefully project Updated", 200);
		return response;
	}

	@Override
	public Response delete(long id) {
		projRepository.deleteById(id);
		Response response = ResponseHelper.responseSender("Succefully project Delete", 200);
		return response;
	}

	@Override
	public Response remove(long pid, String token) {
		Optional<ProjectModel> model = projRepository.findById(pid);
		if (!model.isPresent()) 
			throw new InternalException("No Projects are present ", 401);
		
		long userId = userTokenGenerate.varifyToken(token);
		if (userId == 0)
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());

		if (model.get().getUserId() == userId) {
			    model.get().setUserId(0l);
				projRepository.save(model.get());
				Response response = ResponseHelper.responseSender("Succefully user Deleted", HttpStatus.OK.value());
				return response;
			} else {
				Response response = ResponseHelper.responseSender("user not Deleted from project ", 403);
				return response;
			}
	
	}

	@Override
	public ProjectModel findProject(long pid) {
		Optional<ProjectModel> model = projRepository.findById(pid);
		if (model.isPresent()) {
			return model.get();
		} else
			throw new InternalException("No project are present ", 402);

	}

}
