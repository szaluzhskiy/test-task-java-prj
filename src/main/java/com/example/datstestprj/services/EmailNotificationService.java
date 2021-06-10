package com.example.datstestprj.services;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {
    @Override
    public void notify(String message) {
        System.out.println("Notify by email " + message);
    }
}
