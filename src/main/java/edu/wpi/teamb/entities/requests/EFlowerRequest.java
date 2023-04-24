package edu.wpi.teamb.entities.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DButils;

public class EFlowerRequest extends RequestImpl {
    private String flowerType;
    private String color;
    private String size;
    private String message;

    public EFlowerRequest(String flowerType, String color, String size, String message) {
        this.flowerType = flowerType;
        this.color = color;
        this.size = size;
        this.message = message;
    }

    public EFlowerRequest() {
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

    @Override
    public RequestType getRequestType() {
        return RequestType.FlowerDelivery;
    }

    @Override
    public void submitRequest(String[] inputs) {
        Repository.getRepository().addFlowerRequest(inputs);
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
        return this.color != null && this.flowerType != null && this.size != null && this.message != null;
    }

}