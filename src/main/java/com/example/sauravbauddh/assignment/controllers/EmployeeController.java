package com.example.sauravbauddh.assignment.controllers;

import com.example.sauravbauddh.assignment.entities.Employee;
import com.example.sauravbauddh.assignment.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        // Check if the provided employee object is valid
        if (employee == null || employee.getId() != null) {
            throw new IllegalArgumentException("Invalid employee data. Employee ID should not be provided.");
        }

        String employeeId = employeeService.addEmployee(employee);
        return ResponseEntity.ok(employeeId);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String employeeId) throws ExecutionException, InterruptedException {
        // Check if the employeeId is valid (e.g., not null and not empty)
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<Employee>> getEmployeesWithPaginationAndSorting(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam String sortBy
    ) {
        // Add checks for valid page, pageSize, and sortBy values if necessary

        List<Employee> employees = employeeService.getEmployeesWithPaginationAndSorting(page, pageSize, sortBy);
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        // Check if the employeeId is valid (e.g., not null and not empty)
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<Void> updateEmployee(@PathVariable String employeeId, @RequestBody Employee updatedEmployee) {
        // Check if the provided employee object is valid
        if (updatedEmployee == null) {
            throw new IllegalArgumentException("Invalid employee data. Provide correct data.");
        }

        employeeService.updateEmployeeById(employeeId, updatedEmployee);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{employeeId}/manager")
    public ResponseEntity<Employee> getNthLevelManager(@PathVariable String employeeId, @RequestParam(required = false, defaultValue = "1") int level) {
        // Check if the employeeId is valid (e.g., not null and not empty)
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        Employee manager = employeeService.getNthLevelManager(employeeId, level);
        if (manager != null) {
            return ResponseEntity.ok(manager);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
