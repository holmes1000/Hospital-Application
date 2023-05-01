package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;
import java.sql.Timestamp;

public class Alert {
    private int id;
    private String title;
    private String description;
    private Timestamp createdAt;
    private String employee;

    public Alert() {
        this.id = 0;
        this.title = "";
        this.description = "";
        this.createdAt = null;
        this.employee = "";
    }

    public Alert(int id, String title, String description, Timestamp created_at, String employee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = created_at;
        this.employee = employee;
    }

    public Alert(ResultSet rs) {
        try {
            this.id = rs.getInt("id");
            this.title = rs.getString("title");
            this.description = rs.getString("description");
            this.createdAt = rs.getTimestamp("created_at");
            this.employee = rs.getString("employee");
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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the employee
     */
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", employee='" + employee + '\'';
    }
}
