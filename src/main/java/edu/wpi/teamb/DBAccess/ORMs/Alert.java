package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class Alert {
    private int id;
    private String title;
    private String description;
    private Timestamp createdAt;

    public Alert() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.createdAt = null;
    }

    public Alert(int id, String title, String description, Timestamp created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = created_at;
    }

    public Alert(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.title = rs.getString("title");
            this.description = rs.getString("description");
            this.createdAt = rs.getTimestamp("created_at");
        } catch (Exception e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreated_at(Timestamp created_at) {
        this.createdAt = created_at;
    }
}
