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

import com.lp.pmsProjectService.dto.TaskDto;
import com.lp.pmsProjectService.model.TaskModel;
import com.lp.pmsProjectService.respone.Response;
import com.lp.pmsProjectService.service.TaskService;



@RestController
@RequestMapping("/task")
@CrossOrigin(allowedHeaders = "*",origins = "*")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	

	@PostMapping("/createtask")
	public ResponseEntity<Response> create(@Valid @RequestBody TaskDto dto) {
		Response response = taskService.createTask(dto);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("/getalltasks")
	public List<TaskModel> getTask() {
		List<TaskModel> data = taskService.getData();
		return data;
	}
	
	@PutMapping("/asigntask")
	public ResponseEntity<Response> addTask( @RequestParam int pid , @RequestParam int tid) {
		Response response = taskService.asignTask(pid, tid);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@PutMapping("/asigntaskToUser")
	public ResponseEntity<Response> addTaskToUser( @RequestHeader String token , @RequestParam int tid) {
		Response response = taskService.asignTaskUser(token, tid);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	
	@GetMapping("/getProjectTask")
	public List<TaskModel> getProjectTask(@RequestParam int pid){
		List<TaskModel> model =taskService.getListTask(pid);
		return model;
	}

	@GetMapping("/getUserTask")
	public List<TaskModel> getUserTask(@RequestParam String mail){
		List<TaskModel> model =taskService.getTask(mail);
		return model;
	}
	
	@PutMapping("/changeStatus")
	public ResponseEntity<Response> changeStatus(@RequestParam int tid){
		Response response = taskService.changeStatus(tid);
		return  new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DeleteMapping("/deletetask/{id}")
	public ResponseEntity<Response> deleteTask(@PathVariable int id) {
		Response response = taskService.delete(id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@PutMapping("/updatetask/{id}")
	public ResponseEntity<Response> updateTask(@Valid @RequestBody TaskDto dto, @PathVariable int id) {
		Response response = taskService.update(dto, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}
	
	@DeleteMapping("/removeTaskfromProj")
	public ResponseEntity<Response> removeProject(@RequestParam int pid ,@RequestParam int tid) {
		Response response = taskService.remove(pid , tid);
		return new ResponseEntity<Response>(response, HttpStatus.OK);

	}

}
