package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.ScheduleController;
import com.cst438.controller.StudentController;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {StudentController.class})
public class Cst4380wRegistrationApplicationTests {
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	public static final String TEST_EMAIL = "junit@csumb.edu";
	public static final String TEST_NAME = "junit";
	public static final int TEST_STATUS_CODE = 0;
	public static final int TEST_STUDENT_ID = 0;
	static final String URL = "https://localhost:8080";
	
	@Test
	public void addExistingStudent() throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_EMAIL);
		student.setName(TEST_NAME);
		student.setStatusCode(TEST_STATUS_CODE);
		
		// Verify the student has been added
	    given(studentRepository.findByEmail(TEST_EMAIL)).willReturn(student);
	
	    // Create a DTO for same student
	    StudentDTO studentDTO = new StudentDTO();
	    studentDTO.student_id = TEST_STUDENT_ID;
	    studentDTO.email = TEST_EMAIL;
	    studentDTO.name = TEST_NAME;
	    studentDTO.statusCode = TEST_STATUS_CODE;
	    
	    // Perform mock response
	    response = mvc.perform(
	    		MockMvcRequestBuilders
	    			.post("/student/")
	    			.content(asJsonString(studentDTO))
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON))
	    		.andReturn().getResponse();
	    
	    System.out.println(response.getContentAsString());
	    
	    // Verify 400 error is thrown because student with TEST_EMAIL already exists
	    assertEquals(400, response.getStatus());
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
