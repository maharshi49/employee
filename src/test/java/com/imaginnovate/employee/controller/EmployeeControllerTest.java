package com.imaginnovate.employee.controller;

import com.imaginnovate.employee.entity.Employee;
import com.imaginnovate.employee.exception.CustomEmployeeException;
import com.imaginnovate.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testAddEmployee_Success() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDoj(LocalDate.now());
        employee.setSalary(50000);

        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\":\"E123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"doj\":\"2023-05-16\",\"salary\":50000}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddEmployee_ValidationFailure() throws Exception {
        when(employeeService.addEmployee(any(Employee.class))).thenThrow(new CustomEmployeeException("Validation failed", "100"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"employeeId\":\"E123\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"doj\":\"2023-05-16\",\"salary\":50000}"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void testGetTaxDeductions() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDoj(LocalDate.of(2023, 5, 16));
        employee.setSalary(50000.0);

        Map<String, Object> taxDetails = new HashMap<>();
        taxDetails.put("taxAmount", 37500.0);
        taxDetails.put("cessAmount", 6000.0);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("employeeId", employee.getEmployeeId());
        expectedResponse.put("firstName", employee.getFirstName());
        expectedResponse.put("lastName", employee.getLastName());
        expectedResponse.put("yearlySalary", 600000.0);
        expectedResponse.put("taxAmount", taxDetails.get("taxAmount"));
        expectedResponse.put("cessAmount", taxDetails.get("cessAmount"));
        
        when(employeeService.getTaxDeductionsResponse(employee)).thenReturn(expectedResponse);

        String expectedJsonResponse = """
            {
                "employeeId": "E123",
                "firstName": "John",
                "lastName": "Doe",
                "yearlySalary": 600000,
                "taxAmount": 37500,
                "cessAmount": 6000
            }
            """;
        mockMvc.perform(get("/api/employees/E123/tax-deductions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }

}
