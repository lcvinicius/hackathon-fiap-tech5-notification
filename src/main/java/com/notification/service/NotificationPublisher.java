package com.notification.service;

public interface NotificationPublisher {

    void publishEmail(String recipientEmail, String subject, String message) throws Exception;
}
