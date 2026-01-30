package com.notification.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.notification.model.MedicationArrivedEvent;

public class EventParser {

    private final ObjectMapper objectMapper;

    public EventParser() {
        this.objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Accept ids as numbers in the incoming JSON (common in MVP demos) and coerce to String.
        objectMapper.coercionConfigFor(LogicalType.Textual)
            .setCoercion(CoercionInputShape.Integer, CoercionAction.TryConvert)
            .setCoercion(CoercionInputShape.Float, CoercionAction.TryConvert);
    }

    public MedicationArrivedEvent parseMedicationArrivedEvent(String body) throws Exception {
        return objectMapper.readValue(body, MedicationArrivedEvent.class);
    }
}
