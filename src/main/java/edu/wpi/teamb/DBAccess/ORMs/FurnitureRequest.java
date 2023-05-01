package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;

public class FurnitureRequest {
    private int id;
    private String type;
    private String model;
    private boolean assembly;

    public FurnitureRequest() {
        this.id = 0;
        this.type = "";
        this.model = "";
        this.assembly = false;
    }

    public FurnitureRequest(int id, String type, String model, boolean assembly) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.assembly = assembly;
    }

    public FurnitureRequest(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getInt("id"),
                rs.getString("type"),
                rs.getString("model"),
                rs.getBoolean("assembly"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAssembly() {
        return assembly;
    }

    public void setAssembly(boolean assembly) {
        this.assembly = assembly;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", assembly=" + assembly;
    }
}
