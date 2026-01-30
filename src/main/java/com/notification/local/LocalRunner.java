package com.notification.local;

import com.notification.db.DataSourceProvider;
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
        String body = "{\"medicineId\":\"med-123\",\"medicineName\":\"Dipirona\",\"ubsId\":\"ubs-456\",\"ubsName\":\"UBS Centro\",\"extra\":\"ignored\"}";

        try {
            EventParser parser = new EventParser();
            MedicationArrivedEvent event = parser.parseMedicationArrivedEvent(body);
            logger.info("parsed local event: {}", event);

            boolean runDbTest = "true".equalsIgnoreCase(System.getenv("RUN_DB_TEST"));
            if (runDbTest) {
                if (System.getenv("SNS_DISABLED") == null && System.getProperty("SNS_DISABLED") == null) {
                    logger.info("SNS_DISABLED not set; defaulting to true for local runs (JVM property)");
                    System.setProperty("SNS_DISABLED", "true");
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
}
