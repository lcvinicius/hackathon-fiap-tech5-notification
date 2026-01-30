package com.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationArrivedEvent {

    private String medicineId;
    private String medicineName;
    private String ubsId;
    private String ubsName;

    public MedicationArrivedEvent() {
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getUbsId() {
        return ubsId;
    }

    public void setUbsId(String ubsId) {
        this.ubsId = ubsId;
    }

    public String getUbsName() {
        return ubsName;
    }

    public void setUbsName(String ubsName) {
        this.ubsName = ubsName;
    }

    @Override
    public String toString() {
        return "MedicationArrivedEvent{" +
                "medicineId='" + medicineId + '\'' +
                ", medicineName='" + medicineName + '\'' +
                ", ubsId='" + ubsId + '\'' +
                ", ubsName='" + ubsName + '\'' +
                '}';
    }
}
