package com.example.datstestprj.services;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
    public void notify(String message);
}
