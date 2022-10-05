package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 * add a new student to the system
	 */	
	@PostMapping("/student")
	public StudentDTO addStudent(@RequestBody StudentDTO s) {
		
		if(studentRepository.findByEmail(s.email) != null) {
			System.out.println("/student email already exists: "+s.email);
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student could not be added. Email <"+s.email+"> already exists.");
		} else {
			Student student = new Student();
			student.setEmail(s.email);
			student.setName(s.name);
			student.setStatusCode(s.statusCode);
			
			studentRepository.save(student);
			
			return s;
		}
		
	}
	
	@PutMapping("/student/{id}") // {id} is passed into studentId, status is taken from "?status=" in url
	public void updateStudent( @PathVariable("id") int studentId, @RequestParam("status") int status) {

		Student s = studentRepository.findById(studentId).get();
		
		if(s == null) {
			System.out.println("/student studentId does not exist: "+studentId);
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Student could not be updated. Student <"+studentId+">does not exist.");
		}

		s.setStatusCode(status);
		
		studentRepository.save(s); // updates s
		
	}
	
	
}
