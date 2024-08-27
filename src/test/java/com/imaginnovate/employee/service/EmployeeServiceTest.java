package com.imaginnovate.employee.service;

import com.imaginnovate.employee.entity.Employee;
import com.imaginnovate.employee.exception.CustomEmployeeException;
import com.imaginnovate.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddEmployee_Success() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDoj(LocalDate.now());
        employee.setSalary(50000);

        when(employeeRepository.existsById("E123")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.addEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals("E123", savedEmployee.getEmployeeId());
    }

    @Test
    public void testAddEmployee_EmployeeExists() {
        Employee employee = new Employee();
        employee.setEmployeeId("E123");

        when(employeeRepository.existsById("E123")).thenReturn(true);

        CustomEmployeeException exception = assertThrows(CustomEmployeeException.class, () -> {
            employeeService.addEmployee(employee);
        });

        assertEquals("109", exception.getErrorCode());
    }

    @Test
    public void testCalculateYearlySalary() {
        Employee employee = new Employee();
        employee.setDoj(LocalDate.of(2023, 5, 1));
        employee.setSalary(50000);

        double yearlySalary = employeeService.calculateYearlySalary(employee);

        assertEquals(400000, yearlySalary);
    }

    @Test
    public void testCalculateTaxDeductions() {
        Employee employee = new Employee();
        employee.setDoj(LocalDate.of(2023, 5, 1));
        employee.setSalary(50000);

        var taxDetails = employeeService.getTaxDeductionsResponse(employee);

        assertEquals(15000, taxDetails.get("taxAmount"));
        assertEquals(0, taxDetails.get("cessAmount"));
    }
}
