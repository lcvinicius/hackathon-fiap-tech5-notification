package com.notification.service;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;

public class SnsSmsPublisher implements NotificationPublisher {

    private final SnsClient snsClient;

    public SnsSmsPublisher(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @Override
    public void publishSms(String phoneNumber, String message) {
        Map<String, MessageAttributeValue> attributes = new HashMap<>();

        String smsType = System.getenv("SNS_SMS_TYPE");
        if (smsType != null && !smsType.isBlank()) {
            attributes.put("AWS.SNS.SMS.SMSType", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(smsType)
                    .build());
        }

        String senderId = System.getenv("SNS_SENDER_ID");
        if (senderId != null && !senderId.isBlank()) {
            attributes.put("AWS.SNS.SMS.SenderID", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue(senderId)
                    .build());
        }

        PublishRequest request = PublishRequest.builder()
                .phoneNumber(phoneNumber)
                .message(message)
                .messageAttributes(attributes)
                .build();

        snsClient.publish(request);
    }
}
