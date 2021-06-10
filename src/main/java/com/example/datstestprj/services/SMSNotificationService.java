package com.example.datstestprj.services;

public class SMSNotificationService implements NotificationService {
    @Override
    public void notify(String message) {
        System.out.println("Notify by sms " + message);
    }
}
