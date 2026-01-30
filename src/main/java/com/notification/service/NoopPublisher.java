package com.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopPublisher implements NotificationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(NoopPublisher.class);

    @Override
    public void publishSms(String phoneNumber, String message) {
        logger.info("[MOCK] Enviando SMS para {}; message={}", phoneNumber, message);
    }
}
