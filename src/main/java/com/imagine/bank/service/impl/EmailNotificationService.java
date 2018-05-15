package com.imagine.bank.service.impl;
import com.imagine.bank.service.NotificationService;
import org.springframework.stereotype.Service;
@Service
public class EmailNotificationService implements NotificationService {
    public void sendAlert(String email, String message) {
        System.out.println(""Sending email to "" + email);
    }
}
