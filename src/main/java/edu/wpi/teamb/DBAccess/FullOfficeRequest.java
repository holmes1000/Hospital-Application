package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullOfficeRequest {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String type;
    String item;
    int quantity;
    String requestType = "Office";

    public FullOfficeRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String type, String notes, String item, int quantity) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.item = item;
        this.quantity = quantity;
        this.type = type;
    }

    public FullOfficeRequest(Request request, OfficeRequest officeRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.type = officeRequest.getType();
        this.item = officeRequest.getItem();
        this.quantity = officeRequest.getQuantity();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public static ArrayList<FullOfficeRequest> listFullOfficeRequests(List<OfficeRequest> ors) {
        ArrayList<FullOfficeRequest> fors = new ArrayList<FullOfficeRequest>();
        for (int i = 0; i < ors.size(); i++) {
            OfficeRequest or = ors.get(i);
            Request r = Request.getRequest(or.getId());
            FullOfficeRequest ofr = new FullOfficeRequest(r, or);
            fors.add(ofr);
        }
        return fors;
    }

    public static FullOfficeRequest getFullOfficeRequest(int id, List<FullOfficeRequest> reqList) {
        for (int i = 0; i < reqList.size(); i++) {
            FullOfficeRequest req = reqList.get(i);
            if (req.getId() == id) {
                return req;
            }
        }
        return null;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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
