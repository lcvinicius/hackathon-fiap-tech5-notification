package com.notification.service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

public class SnsTopicPublisher implements NotificationPublisher {

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsTopicPublisher(SnsClient snsClient, String topicArn) {
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    @Override
    public void publishSms(String phoneNumber, String message) {
        // In "topic" mode, the fan-out is managed by SNS subscriptions (infra).
        // The phoneNumber is ignored by design.
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        snsClient.publish(request);
    }
}
