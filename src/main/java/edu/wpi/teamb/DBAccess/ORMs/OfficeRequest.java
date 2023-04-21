package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfficeRequest {
    int id;
    String type;
    String item;
    int quantity;
    public OfficeRequest(int id, String type, String item, int quantity) {
        this.id = id;
        this.type = type;
        this.item = item;
        this.quantity = quantity;
    }
    public OfficeRequest(ResultSet rs) throws SQLException {
        this (
            rs.getInt("id"),
                rs.getString("type"),
            rs.getString("item"),
            rs.getInt("quantity")
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
