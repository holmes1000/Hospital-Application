package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullTranslationRequest;

public class ETranslationRequest extends RequestImpl {
    private String languageType;
    private String medicalInNature;

    public ETranslationRequest(String languageType, String medicalInNature) {
        this.languageType = languageType;
        this.medicalInNature = medicalInNature;
    }

    public ETranslationRequest() {
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

    public String getMedicalInNature() {
        return medicalInNature;
    }

    public void setMedicalInNature(String medicalInNature) {
        this.medicalInNature = medicalInNature;
    }
    @Override
    public RequestType getRequestType() {
        return RequestType.TranslationRequest;
    }

    @Override
    public void submitRequest(String[] inputs) {
        Repository.getRepository().addTranslationRequest(inputs);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public boolean checkSpecialRequestFields() {
        return this.medicalInNature != null && this.languageType != null;
    }

    public void updateTranslationRequest(FullTranslationRequest fullTranslationRequest) {
        Repository.getRepository().updateTranslationRequest(fullTranslationRequest);
    }

    public void deleteTranslationRequest(FullTranslationRequest fullTranslationRequest) {
        Repository.getRepository().deleteTranslationRequest(fullTranslationRequest);
    }

}
