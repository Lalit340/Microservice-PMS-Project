package com.lp.pmsProjectService.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lp.pmsProjectService.dto.ProjectDto;
import com.lp.pmsProjectService.model.ProjectModel;
import com.lp.pmsProjectService.respone.Response;
import com.lp.pmsProjectService.service.ProjectService;



@RestController
@RequestMapping("/project")
@CrossOrigin(allowedHeaders = "*",origins = "*")
public class ProjectController {
	
	@Autowired
	private ProjectService service;
	
	@PostMapping("/createProject")
	public ResponseEntity<Response> create(@Valid @RequestBody ProjectDto dto){
		 Response response=  service.createProject(dto);
		return new ResponseEntity<Response>(response , HttpStatus.OK);
	}

	@GetMapping("/getAllProjects")
	public List<ProjectModel> getProjects(){
		List<ProjectModel> model = service.getProject();
		return model;
	}
	
	@GetMapping("/getOneProjects")
	public ProjectModel getOneProjects(@RequestParam long pid){
		ProjectModel model = service.findProject(pid);
		return model;
	}
	
	@PutMapping("/assignProject")
	public ResponseEntity<Response> assign(@RequestParam long pid ,@RequestHeader String token){
		Response response = service.assignProject(pid, token);
		return new ResponseEntity<Response>(response ,HttpStatus.OK);
		
	}
	
	@GetMapping("/getProject")
	public List<ProjectModel> getUserProjects(@RequestHeader String token){
		List<ProjectModel> projects = service.findUserProject(token);
		return projects;
	}
	
	@PutMapping("/updateProj/{id}")
	public ResponseEntity<Response> updateProject(@Valid @RequestBody ProjectDto dto, @PathVariable int id) {
		Response response = service.update(dto, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@DeleteMapping("/deleteProj/{id}")
	public ResponseEntity<Response> deleteProject(@PathVariable int id) {
		Response response = service.delete(id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@DeleteMapping("/removeProj")
	public ResponseEntity<Response> removeProject(@RequestParam int pid ,@RequestHeader String token) {
		Response response = service.remove(pid , token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
}
