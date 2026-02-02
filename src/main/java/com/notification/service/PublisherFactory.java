package com.notification.service;

import software.amazon.awssdk.services.ses.SesClient;

public class PublisherFactory {

    private PublisherFactory() {
    }

    public static NotificationPublisher fromEnv() {
        boolean disabled = isTrue("EMAIL_DISABLED") || isTrue("SNS_DISABLED");
        if (disabled) {
            return new NoopPublisher();
        }

        String from = getConfig("EMAIL_FROM");
        if (from == null || from.isBlank()) {
            throw new IllegalStateException("EMAIL_FROM must be set when EMAIL_DISABLED=false");
        }

        SesClient sesClient = SesClient.builder().build();
        return new SesEmailPublisher(sesClient, from);
    }

    private static boolean isTrue(String key) {
        return "true".equalsIgnoreCase(getConfig(key));
    }

    private static String getConfig(String key) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null) {
            return fromEnv;
        }
        return System.getProperty(key);
    }
}
