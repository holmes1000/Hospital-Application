package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficeRequest {
    int id;
    String item;
    int quantity;
    String specialInstructions;
    String type;
    public OfficeRequest(int id, String item, int quantity, String specialInstructions, String type) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.type = type;
    }
    public OfficeRequest(ResultSet rs) throws SQLException {
        this (
            rs.getInt("id"),
            rs.getString("item"),
            rs.getInt("quantity"),
            rs.getString("specialinstructions"),
            rs.getString("type")
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
