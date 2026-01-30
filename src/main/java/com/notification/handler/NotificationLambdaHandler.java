package com.notification.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationLambdaHandler implements RequestHandler<SQSEvent, Void> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLambdaHandler.class);

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        int recordCount = event == null || event.getRecords() == null ? 0 : event.getRecords().size();
        logger.info("notification-service starting; records={}", recordCount);
        logger.info("event received (raw): {}", event);
        logger.info("notification-service finished");
        return null;
    }
}
