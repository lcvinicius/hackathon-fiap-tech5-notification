package com.notification.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationArrivedEvent {

    @JsonAlias({"idMedicamento"})
    private String medicineId;

    @JsonAlias({"nomeMedicamento"})
    private String medicineName;

    @JsonAlias({"idPosto"})
    private String ubsId;

    @JsonAlias({"nomePosto", "nomeUBS", "nomeUbs"})
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
