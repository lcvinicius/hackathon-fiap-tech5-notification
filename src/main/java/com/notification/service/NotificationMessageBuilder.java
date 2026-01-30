package com.notification.service;

import com.notification.model.MedicationArrivedEvent;

public class NotificationMessageBuilder {

    public String buildMessage(MedicationArrivedEvent event) {
        String medicineName = nullToEmpty(event.getMedicineName());
        String ubsName = nullToEmpty(event.getUbsName());
        return "Seu medicamento " + medicineName + " está disponível no posto " + ubsName + ".";
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
