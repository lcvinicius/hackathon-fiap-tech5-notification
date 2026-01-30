package com.notification.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.model.MedicationArrivedEvent;

public class EventParser {

    private final ObjectMapper objectMapper;

    public EventParser() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public MedicationArrivedEvent parseMedicationArrivedEvent(String body) throws Exception {
        return objectMapper.readValue(body, MedicationArrivedEvent.class);
    }
}
