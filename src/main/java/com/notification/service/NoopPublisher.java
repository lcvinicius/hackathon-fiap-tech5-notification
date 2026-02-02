package com.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoopPublisher implements NotificationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(NoopPublisher.class);

    @Override
    public void publishEmail(String recipientEmail, String subject, String message) {
        logger.info("[MOCK] Enviando EMAIL para {}; subject={}; message={}", recipientEmail, subject, message);
    }
}
