package com.example.sauravbauddh.assignment.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {
    private String id;
    private String employeeName;
    private String phoneNumber;
    private String email;
    private String reportsTo;
    private String profileImage;
}