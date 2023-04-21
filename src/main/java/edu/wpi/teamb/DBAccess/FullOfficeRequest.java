package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FullOfficeRequest {
    int id;
    String employee;
    String floor;
    String roomNumber;
    Date dateSubmitted;
    String requestStatus;
    String locationName;
    String item;
    int quantity;
    String specialInstructions;
    String type;
    String requestType = "Office";

    public FullOfficeRequest(int id, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus, String locationName, String item, int quantity, String specialInstructions, String type) {
        this.id = id;
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.item = item;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.type = type;
    }

    public FullOfficeRequest(Request request, OfficeRequest officeRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.floor = request.getFloor();
        this.roomNumber = request.getRoomNumber();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.type = officeRequest.getType();
        this.item = officeRequest.getItem();
        this.quantity = officeRequest.getQuantity();
        this.specialInstructions = officeRequest.getSpecialInstructions();
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }


    public static ArrayList<FullOfficeRequest> listFullOfficeRequests(List<OfficeRequest> ors) {
        ArrayList<FullOfficeRequest> fors = new ArrayList<FullOfficeRequest>();
        for (int i = 0; i < ors.size(); i++) {
            OfficeRequest or = ors.get(i);
            Request r = RequestDAOImpl.getRequest(or.getId());
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
