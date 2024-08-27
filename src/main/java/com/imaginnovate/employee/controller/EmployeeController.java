package com.imaginnovate.employee.controller;

import com.imaginnovate.employee.entity.Employee;
import com.imaginnovate.employee.exception.CustomEmployeeException;
import com.imaginnovate.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.addEmployee(employee);
            return ResponseEntity.ok(savedEmployee);
        }catch (CustomEmployeeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", e.getMessage());
            errorResponse.put("errorCode", e.getErrorCode());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
        	return ResponseEntity.badRequest().body("Error saving employee: " + e.getMessage());
        }
    }

    @GetMapping("/{employeeId}/tax-deductions")
    public ResponseEntity<?> getTaxDeductions(@PathVariable String employeeId) {
        Optional<Employee> employeeOpt = employeeService.getEmployeeById(employeeId);
        if (!employeeOpt.isPresent()) {
            return ResponseEntity.status(404).body("Employee not found");
        }
        Employee employee = employeeOpt.get();
        try {
            Map<String, Object> taxDeductions = employeeService.getTaxDeductionsResponse(employee);
            return ResponseEntity.ok(taxDeductions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calculating tax deductions: " + e.getMessage());
        }
    }
}

