package com.example.sauravbauddh.assignment.services;

import com.example.sauravbauddh.assignment.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom; // The email address from which emails will be sent

    public void sendNewEmployeeNotification(String managerEmail, Employee newEmployee) {
        // Add checks for valid managerEmail and newEmployee objects
        if (managerEmail == null || managerEmail.isEmpty()) {
            throw new IllegalArgumentException("Invalid manager email provided.");
        }
        if (newEmployee == null || newEmployee.getEmployeeName() == null || newEmployee.getEmail() == null) {
            throw new IllegalArgumentException("Invalid new employee data provided.");
        }

        String subject = "New Employee Notification";
        String message = String.format(
                "%s will now work under you. Mobile number is %s and email is %s.",
                newEmployee.getEmployeeName(), newEmployee.getPhoneNumber(), newEmployee.getEmail()
        );

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailFrom);
            mailMessage.setTo(managerEmail);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error sending the new employee notification email.");
        }
    }
}
