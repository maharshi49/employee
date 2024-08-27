package com.imaginnovate.employee.validator;

import com.imaginnovate.employee.entity.Employee;
import com.imaginnovate.employee.exception.CustomEmployeeException;

import java.util.List;
import java.util.regex.Pattern;

public class EmployeeValidator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static void validateEmployee(Employee employee) {
        validateEmployeeId(employee.getEmployeeId());
        validateName(employee.getFirstName(), employee.getLastName());
        validateEmail(employee.getEmail());
        validatePhoneNumbers(employee.getPhoneNumbers());
        validateDoj(employee.getDoj());
        validateSalary(employee.getSalary());
    }

    private static void validateEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isEmpty()) {
            throw new CustomEmployeeException("Employee ID cannot be null or empty", "101");
        }
    }
    
    private static void validateName(String firstName, String LastName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new CustomEmployeeException("FirstName cannot be null or empty", "102");
        }
        
        if (LastName == null || LastName.isEmpty()) {
            throw new CustomEmployeeException("LastName cannot be null or empty", "103");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomEmployeeException("Invalid email format", "104");
        }
    }

    private static void validatePhoneNumbers(List<String> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            throw new CustomEmployeeException("Phone numbers cannot be empty", "105");
        }
        for (String phoneNumber : phoneNumbers) {
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                throw new CustomEmployeeException("Invalid phone number format: " + phoneNumber, "106");
            }
        }
    }

    private static void validateDoj(java.time.LocalDate doj) {
        if (doj == null) {
            throw new CustomEmployeeException("Date of joining cannot be null", "107");
        }
    }

    private static void validateSalary(double salary) {
        if (salary <= 0) {
            throw new CustomEmployeeException("Salary must be a positive number", "108");
        }
    }
}