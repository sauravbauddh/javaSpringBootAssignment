package com.example.sauravbauddh.assignment.repositories;

import com.example.sauravbauddh.assignment.entities.Employee;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeRepository {
    String addEmployee(Employee employee);
    List<Employee> getAllEmployees();

    List<Employee> getEmployeesWithPaginationAndSorting(int page, int pageSize, String sortBy);

    Employee getNthLevelManager(String employeeId, int level);
    Employee getEmployeeById(String employeeId) throws ExecutionException, InterruptedException;
    void deleteEmployeeById(String employeeId);
    void updateEmployeeById(String employeeId, Employee updatedEmployee);
}