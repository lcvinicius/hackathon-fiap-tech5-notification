package com.notification.service;

public interface NotificationPublisher {

    void publishSms(String phoneNumber, String message) throws Exception;
}
