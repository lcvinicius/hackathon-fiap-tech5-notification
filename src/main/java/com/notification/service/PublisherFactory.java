package com.notification.service;

import software.amazon.awssdk.services.sns.SnsClient;

public class PublisherFactory {

    private PublisherFactory() {
    }

    public static NotificationPublisher fromEnv() {
        boolean disabled = "true".equalsIgnoreCase(getConfig("SNS_DISABLED"));
        if (disabled) {
            return new NoopPublisher();
        }

        String mode = getConfig("SNS_PUBLISH_TARGET");
        if (mode == null || mode.isBlank()) {
            mode = "phone";
        }

        SnsClient snsClient = SnsClient.builder().build();

        if ("topic".equalsIgnoreCase(mode)) {
            String topicArn = getConfig("SNS_TOPIC_ARN");
            if (topicArn == null || topicArn.isBlank()) {
                throw new IllegalStateException("SNS_TOPIC_ARN must be set when SNS_PUBLISH_TARGET=topic");
            }
            return new SnsTopicPublisher(snsClient, topicArn);
        }

        return new SnsSmsPublisher(snsClient);
    }

    private static String getConfig(String key) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null) {
            return fromEnv;
        }
        return System.getProperty(key);
    }
}
