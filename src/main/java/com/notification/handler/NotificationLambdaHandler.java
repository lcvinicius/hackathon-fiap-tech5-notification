package com.notification.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.notification.db.DataSourceProvider;
import com.notification.model.MedicationArrivedEvent;
import com.notification.parser.EventParser;
import com.notification.repository.JdbcSubscriberRepository;
import com.notification.service.NotificationMessageBuilder;
import com.notification.service.NotificationProcessor;
import com.notification.service.PublisherFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationLambdaHandler implements RequestHandler<SQSEvent, Void> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLambdaHandler.class);
    private final EventParser eventParser;
    private final NotificationProcessor processor;

    public NotificationLambdaHandler() {
        this.eventParser = new EventParser();
        this.processor = new NotificationProcessor(
                new JdbcSubscriberRepository(DataSourceProvider.get()),
                new NotificationMessageBuilder(),
                PublisherFactory.fromEnv()
        );
    }

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        int recordCount = event == null || event.getRecords() == null ? 0 : event.getRecords().size();
        logger.info("notification-service starting; records={}", recordCount);
        if (event == null || event.getRecords() == null || event.getRecords().isEmpty()) {
            logger.info("no records to process");
            return null;
        }

        for (SQSEvent.SQSMessage record : event.getRecords()) {
            String messageId = record.getMessageId();
            String body = record.getBody();
            logger.info("processing record; messageId={}", messageId);

            try {
                MedicationArrivedEvent parsed = eventParser.parseMedicationArrivedEvent(body);
                logger.info("parsed event; messageId={}; event={}", messageId, parsed);

                processor.process(parsed);
            } catch (Exception e) {
                logger.error("failed to process record; messageId={}; body={}", messageId, body, e);
            }
        }
        logger.info("notification-service finished");
        return null;
    }
}
