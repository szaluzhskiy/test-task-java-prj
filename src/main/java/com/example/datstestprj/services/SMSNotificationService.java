package com.example.datstestprj.services;

import org.springframework.stereotype.Service;

@Service
public class SMSNotificationService implements NotificationService {
    @Override
    public void notify(String message) {
        System.out.println("Notify by sms " + message);
    }
}
