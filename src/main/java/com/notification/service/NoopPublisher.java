package com.notification.service;

public class NoopPublisher implements NotificationPublisher {

    @Override
    public void publishSms(String phoneNumber, String message) {
        // Intentionally no-op for local runs / dry-run.
    }
}
