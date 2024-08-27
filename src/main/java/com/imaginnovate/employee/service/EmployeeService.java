package com.imaginnovate.employee.service;

import com.imaginnovate.employee.entity.Employee;
import com.imaginnovate.employee.exception.CustomEmployeeException;
import com.imaginnovate.employee.repository.EmployeeRepository;
import com.imaginnovate.employee.validator.EmployeeValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    

    public Employee addEmployee(Employee employee) {
    	
    	EmployeeValidator.validateEmployee(employee);
    	
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            throw new CustomEmployeeException("Employee ID already exists", "109");
        }
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(String employeeId) {
        return employeeRepository.findById(employeeId);
    }

    public double calculateYearlySalary(Employee employee) {
        LocalDate currentDate = LocalDate.now();
        LocalDate doj = employee.getDoj();
        int monthsWorked = currentDate.getYear() == doj.getYear()
                ? currentDate.getMonthValue() - doj.getMonthValue() + 1
                : 12 - doj.getMonthValue() + 1;

        double monthlySalary = employee.getSalary();
        return monthlySalary * monthsWorked;
    }

    public Map<String, Object> getTaxDeductionsResponse(Employee employee) {
    	double yearlySalary = calculateYearlySalary(employee);
        double taxAmount = 0.0;
        double cessAmount = 0.0;

        if (yearlySalary <= 250000) {
            taxAmount = 0.0;
        } else if (yearlySalary <= 500000) {
            taxAmount = (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            taxAmount = (250000 * 0.05) + (yearlySalary - 500000) * 0.10;
        } else {
            taxAmount = (250000 * 0.05) + (500000 * 0.10) + (yearlySalary - 1000000) * 0.20;
        }

        if (yearlySalary > 2500000) {
            cessAmount = (yearlySalary - 2500000) * 0.02;
        }

        Map<String, Object> taxDetails = new HashMap<>();
        taxDetails.put("employeeId", employee.getEmployeeId());
        taxDetails.put("firstName", employee.getFirstName());
        taxDetails.put("lastName", employee.getLastName());
        taxDetails.put("yearlySalary", yearlySalary);
        taxDetails.put("taxAmount", taxAmount);
        taxDetails.put("cessAmount", cessAmount);

        return taxDetails;
    }
}
