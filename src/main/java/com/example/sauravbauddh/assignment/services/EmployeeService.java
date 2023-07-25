package com.example.sauravbauddh.assignment.services;

import com.example.sauravbauddh.assignment.entities.Employee;
import com.example.sauravbauddh.assignment.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String addEmployee(Employee employee) {
        // Add checks for valid employee object and required fields if necessary
        if (employee == null) {
            throw new IllegalArgumentException("Invalid employee data. Employee object is null.");
        }

        return employeeRepository.addEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    public void deleteEmployeeById(String employeeId) {
        // Add checks for valid employeeId value if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        employeeRepository.deleteEmployeeById(employeeId);
    }

    public void updateEmployeeById(String employeeId, Employee updatedEmployee) {
        // Add checks for valid employeeId and updatedEmployee object if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Invalid updated employee data. Employee object is null.");
        }

        employeeRepository.updateEmployeeById(employeeId, updatedEmployee);
    }

    public Employee getEmployeeById(String employeeId) throws ExecutionException, InterruptedException {
        // Add checks for valid employeeId value if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        return employeeRepository.getEmployeeById(employeeId);
    }

    public Employee getNthLevelManager(String employeeId, int level) {
        // Add checks for valid employeeId and level values if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }
        if (level <= 0) {
            throw new IllegalArgumentException("Invalid level value. It should be greater than 0.");
        }

        return employeeRepository.getNthLevelManager(employeeId, level);
    }

    public List<Employee> getEmployeesWithPaginationAndSorting(int page, int pageSize, String sortBy) {
        // Add checks for valid page, pageSize, and sortBy values if necessary
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page or pageSize value. Both should be greater than 0.");
        }

        return employeeRepository.getEmployeesWithPaginationAndSorting(page, pageSize, sortBy);
    }
}
