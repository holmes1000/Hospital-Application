package edu.wpi.teamb.entities.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.Full.FullOfficeRequest;

public class EOfficeRequest extends RequestImpl {
    private String type;
    private String item;
    private int quantity;

    public EOfficeRequest(String type, String item, int quantity) {
        this.type = type;
        this.item = item;
        this.quantity = quantity;
    }

    public EOfficeRequest() {
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.OfficeSupplies;
    }

    @Override
    public void submitRequest(String[] inputs) {
        Repository.getRepository().addOfficeRequest(inputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EOfficeRequest eOfficeRequest)) {
            return false;
        }
        return Objects.equals(type, eOfficeRequest.type) && Objects.equals(item, eOfficeRequest.item) && quantity == eOfficeRequest.quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean checkSpecialRequestFields() {
        return this.type != null && this.item != null && this.quantity != 0;
    }

    public void updateOfficeReqeust(FullOfficeRequest fullOfficeRequest) {
        Repository.getRepository().updateOfficeRequest(fullOfficeRequest);
    }
}