package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;

public class TranslationRequest {
    private int id;
    private String language;
    private String medical;

    public TranslationRequest() {
        this.id = 0;
        this.language = "";
        this.medical = "";
    }

    public TranslationRequest(int id, String language, String medical) {
        this.id = id;
        this.language = language;
        this.medical = medical;
    }

    public TranslationRequest(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getInt("id"),
                rs.getString("language"),
                rs.getString("medicalimportance"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }
}
