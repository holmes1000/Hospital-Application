package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;

public class FlowerRequest {
    private int id;
    private String flowerType;
    private String color;
    private String size;
    private String message;

    public FlowerRequest() {
        this.id = 0;
        this.flowerType = "";
        this.color = "";
        this.size = "";
        this.message = "";
    }

    public FlowerRequest(int id, String flowerType, String color,String type,String message) {
        this.id = id;
        this.flowerType = flowerType;
        this.color = color;
        this.size = type;
        this.message = message;
    }

    public FlowerRequest(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getInt("id"),
                rs.getString("flowerType"),
                rs.getString("color"),
                rs.getString("size"),
                rs.getString("message"));
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
