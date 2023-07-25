package com.example.sauravbauddh.assignment.repositories;

import com.example.sauravbauddh.assignment.entities.Employee;
import com.example.sauravbauddh.assignment.services.EmailService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Autowired
    private Firestore firestore;


    @Autowired
    private EmailService emailService;
    @Override
    public List<Employee> getEmployeesWithPaginationAndSorting(int page, int pageSize, String sortBy) {
        // Add checks for valid page, pageSize, and sortBy values if necessary
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page or pageSize value. Both should be greater than 0.");
        }

        // Perform pagination and sorting query using Firestore Query
        Query query = firestore.collection("employees")
                .orderBy(sortBy)
                .offset((page - 1) * pageSize)
                .limit(pageSize);

        try {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<Employee> employees = new ArrayList<>();
            for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
                employees.add(document.toObject(Employee.class));
            }
            return employees;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving employees with pagination and sorting from the database.");
        }
    }

    @Override
    public String addEmployee(Employee employee) {
        // Add checks for valid employee object and required fields if necessary
        if (employee == null) {
            throw new IllegalArgumentException("Invalid employee data. Employee object is null.");
        }

        // Generate a unique UUID for the employee ID
        String employeeId = UUID.randomUUID().toString();
        employee.setId(employeeId);

        // Save the employee data to Firestore
        ApiFuture<WriteResult> future = firestore.collection("employees").document(employeeId).set(employee);

        try {
            // Wait for the future to complete (i.e., the employee is stored successfully)
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Error storing employee data in the database.");
        }

        // Send notification email to Level 1 manager if reportsTo is not "none"
        if (!"none".equalsIgnoreCase(employee.getReportsTo())) {
            Employee manager = getNthLevelManager(employeeId, 1);
            if (manager != null) {
                emailService.sendNewEmployeeNotification(manager.getEmail(), employee);
            }
        }

        return employeeId;
    }

    @Override
    public Employee getNthLevelManager(String employeeId, int level) {
        // Add checks for valid employeeId and level values if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }
        if (level <= 0) {
            throw new IllegalArgumentException("Invalid level value. It should be greater than 0.");
        }

        // If reportsTo is "none", return the employee itself as there is no manager
        Employee employee;
        try {
            employee = getEmployeeById(employeeId);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (employee == null) {
            throw new RuntimeException("Employee not found.");
        }
        if ("none".equalsIgnoreCase(employee.getReportsTo())) {
            return employee;
        }

        // Continue with the existing logic for finding the Nth level manager
        int currentLevel = 0;
        String currentManagerId = employeeId;

        while (currentLevel < level) {
            DocumentSnapshot document;
            try {
                document = firestore.collection("employees").document(currentManagerId).get().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error retrieving manager data from the database.", e);
            }
            if (document.exists()) {
                Employee manager = document.toObject(Employee.class);
                assert manager != null;
                currentManagerId = manager.getReportsTo();
                currentLevel++;
            } else {
                throw new RuntimeException("Employee or manager not found.");
            }
        }

        DocumentSnapshot nthLevelManagerDocument;
        try {
            nthLevelManagerDocument = firestore.collection("employees").document(currentManagerId).get().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error retrieving Nth level manager data from the database.", e);
        }
        if (nthLevelManagerDocument.exists()) {
            return nthLevelManagerDocument.toObject(Employee.class);
        } else {
            throw new RuntimeException("Nth level manager not found.");
        }
    }

    @Override
    public Employee getEmployeeById(String employeeId) throws ExecutionException, InterruptedException {
        // Add checks for valid employeeId value if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        DocumentSnapshot document = firestore.collection("employees").document(employeeId).get().get();
        if (document.exists()) {
            return document.toObject(Employee.class);
        } else {
            return null;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        ApiFuture<QuerySnapshot> query = firestore.collection("employees").get();
        List<Employee> employees = new ArrayList<>();

        try {
            for (DocumentSnapshot document : query.get().getDocuments()) {
                employees.add(document.toObject(Employee.class));
            }
            return employees;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving employees from the database.");
        }
    }

    @Override
    public void deleteEmployeeById(String employeeId) {
        // Add checks for valid employeeId value if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }

        firestore.collection("employees").document(employeeId).delete();
    }

    @Override
    public void updateEmployeeById(String employeeId, Employee updatedEmployee) {
        // Add checks for valid employeeId and updatedEmployee object if necessary
        if (employeeId == null || employeeId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee ID provided.");
        }
        if (updatedEmployee == null || !employeeId.equals(updatedEmployee.getId())) {
            throw new IllegalArgumentException("Invalid updated employee data. Employee ID should match the path parameter.");
        }

        firestore.collection("employees").document(employeeId).set(updatedEmployee, SetOptions.merge());
    }
}
