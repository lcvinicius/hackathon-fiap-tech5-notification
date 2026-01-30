package com.notification.local;

import com.notification.model.MedicationArrivedEvent;
import com.notification.parser.EventParser;
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
        } catch (Exception e) {
            logger.error("failed to parse local event", e);
            System.exit(1);
        }
    }
}
