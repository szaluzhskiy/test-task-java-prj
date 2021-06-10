package com.example.datstestprj.services;

public class EmailNotificationService implements NotificationService {
    @Override
    public void notify(String message) {
        System.out.println("Notify by email " + message);
    }
}
