package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndStudentTest {
	public static final String EDGE_DRIVER_FILE_LOCATION = "C:/edgedriver_win64/msedgedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_NAME = "test"; 
	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		// If the student exists, delete it
		Student s = null;
		do {
			s = studentRepository.findByEmail(TEST_USER_EMAIL);
			System.out.println(s);
			if(s != null)
				studentRepository.delete(s);
		} while (s != null);
		
		// Add the test student
		
		System.setProperty("webdriver.edge.driver", EDGE_DRIVER_FILE_LOCATION);
		WebDriver driver = new EdgeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// Locate and click List Students button
			WebElement we = driver.findElement(By.xpath("//a[@name='listStudents']"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// Locate and click Add student button
			driver.findElement(By.xpath("//button[@name='addStudent']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Enter name and email and click Add button
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.xpath("//button[@name='add']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Verify student row has been inserted into database
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(student, "Student not found in database");
			
		} catch (Exception ex) {
			throw ex;
		} finally { // reset database
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if(student != null) 
				studentRepository.delete(student);
			
			driver.quit();
		}
	}
	
	
	
}
