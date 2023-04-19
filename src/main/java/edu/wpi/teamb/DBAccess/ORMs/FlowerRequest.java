package edu.wpi.teamb.DBAccess.ORMs;

import java.lang.ProcessBuilder.Redirect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.wpi.teamb.DBAccess.DB;

public class FlowerRequest {
    private int id;
    private String flowerType;
    private String color;
    private String type;
    private String message;
    private String specialInstructions;

    public FlowerRequest() {
        this.id = 0;
        this.flowerType = "";
        this.color = "";
        this.specialInstructions = "";
    }

    public FlowerRequest(int id, String flowerType, String color,String type,String message, String specialInstructions) {
        this.id = id;
        this.flowerType = flowerType;
        this.color = color;
        this.type = type;
        this.message = message;
        this.specialInstructions = specialInstructions;
    }

    public FlowerRequest(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getInt("id"),
                rs.getString("flowerType"),
                rs.getString("color"),
                rs.getString("type"),
                rs.getString("message"),
                rs.getString("specialInstructions"));
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
