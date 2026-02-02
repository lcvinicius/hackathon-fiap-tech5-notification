package com.notification.local;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.notification.db.DataSourceProvider;
import com.notification.handler.NotificationLambdaHandler;
import com.notification.model.MedicationArrivedEvent;
import com.notification.parser.EventParser;
import com.notification.repository.JdbcSubscriberRepository;
import com.notification.service.NotificationMessageBuilder;
import com.notification.service.NotificationProcessor;
import com.notification.service.PublisherFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalRunner {

    private static final Logger logger = LoggerFactory.getLogger(LocalRunner.class);

    public static void main(String[] args) {
        String mode = getConfig("LOCAL_MODE");
        if (mode == null || mode.isBlank()) {
            mode = "processor";
        }

        String body = getConfig("EVENT_JSON");
        if (body == null || body.isBlank()) {
            body = "{\"idMedicamento\":\"med-123\",\"nomeMedicamento\":\"Dipirona\",\"idPosto\":\"ubs-456\",\"nomePosto\":\"UBS Centro\",\"extra\":\"ignored\"}";
        }

        try {
            if ("handler".equalsIgnoreCase(mode)) {
                runHandlerMode(body);
                return;
            }

            EventParser parser = new EventParser();
            MedicationArrivedEvent event = parser.parseMedicationArrivedEvent(body);
            logger.info("parsed local event: {}", event);

            boolean runDbTest = "true".equalsIgnoreCase(getConfig("RUN_DB_TEST"));
            if (runDbTest) {
                if (System.getenv("EMAIL_DISABLED") == null && System.getProperty("EMAIL_DISABLED") == null) {
                    logger.info("EMAIL_DISABLED not set; defaulting to true for local runs (JVM property)");
                    System.setProperty("EMAIL_DISABLED", "true");
                }

                NotificationProcessor processor = new NotificationProcessor(
                        new JdbcSubscriberRepository(DataSourceProvider.get()),
                        new NotificationMessageBuilder(),
                        PublisherFactory.fromEnv()
                );
                processor.process(event);
            } else {
                logger.info("db test skipped (set RUN_DB_TEST=true to enable)");
            }
        } catch (Exception e) {
            logger.error("failed to parse local event", e);
            System.exit(1);
        }
    }

    private static String getConfig(String key) {
        String fromEnv = System.getenv(key);
        if (fromEnv != null) {
            return fromEnv;
        }
        return System.getProperty(key);
    }

    private static void runHandlerMode(String body) {
        if (System.getenv("EMAIL_DISABLED") == null && System.getProperty("EMAIL_DISABLED") == null) {
            logger.info("EMAIL_DISABLED not set; defaulting to true for local runs (JVM property)");
            System.setProperty("EMAIL_DISABLED", "true");
        }

        SQSEvent.SQSMessage msg = new SQSEvent.SQSMessage();
        msg.setMessageId("local-1");
        msg.setBody(body);

        SQSEvent sqsEvent = new SQSEvent();
        sqsEvent.setRecords(java.util.List.of(msg));

        logger.info("running in LOCAL_MODE=handler (simulating SQS -> Lambda handler)");
        new NotificationLambdaHandler().handleRequest(sqsEvent, null);
    }
}
