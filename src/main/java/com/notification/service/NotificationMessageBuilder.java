package com.notification.service;

import com.notification.model.MedicationArrivedEvent;

public class NotificationMessageBuilder {

    public String buildSubject(MedicationArrivedEvent event) {
        String medicine = pick(event.getMedicineName(), event.getMedicineId());
        if (medicine == null || medicine.isBlank()) {
            return "Medicamento disponível";
        }
        return "Medicamento disponível: " + medicine;
    }

    public String buildMessage(MedicationArrivedEvent event) {
        String medicine = pick(event.getMedicineName(), event.getMedicineId());
        String posto = pick(event.getUbsName(), event.getUbsId());
        return "Seu medicamento " + medicine + " está disponível no posto " + posto + ".";
    }

    private static String pick(String preferred, String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred;
        }
        if (fallback != null && !fallback.isBlank()) {
            return fallback;
        }
        return "";
    }
}
