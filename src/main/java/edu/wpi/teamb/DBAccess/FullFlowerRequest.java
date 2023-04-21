package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FullFlowerRequest {
    int id;
    String employee;
    String floor;
    String roomNumber;
    Date dateSubmitted;
    String requestStatus;
    String location_name;
    String flowerType;
    String color;
    String type;
    String message;
    String specialInstructions;
    String requestType = "Flower";

    public FullFlowerRequest(int id, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus, String location_name, String flowerType, String color, String type, String message, String specialInstructions) {
        this.id = id;
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.location_name = location_name;
        this.flowerType = flowerType;
        this.color = color;
        this.type = type;
        this.message = message;
        this.specialInstructions = specialInstructions;
    }
    public FullFlowerRequest(Request r, FlowerRequest f) {
        this.id = r.getId();
        this.employee = r.getEmployee();
        this.floor = r.getFloor();
        this.roomNumber = r.getRoomNumber();
        this.dateSubmitted = r.getDateSubmitted();
        this.requestStatus = r.getRequestStatus();
        this.location_name = r.getLocationName();
        this.flowerType = f.getFlowerType();
        this.color = f.getColor();
        this.type = f.getType();
        this.message = f.getMessage();
        this.specialInstructions = f.getSpecialInstructions();
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
    public static ArrayList<FullFlowerRequest> listFullFlowerRequests(List<FlowerRequest> frs) {
        ArrayList<FullFlowerRequest> ffrs = new ArrayList<FullFlowerRequest>();
        for (int i = 0; i < frs.size(); i++) {
            FlowerRequest fr = frs.get(i);
            Request r = RequestDAOImpl.getRequest(fr.getId());
            FullFlowerRequest ffr = new FullFlowerRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

    public static FullFlowerRequest getFullFlowerRequest(int id, List<FullFlowerRequest> reqList) {
        for (int i = 0; i < reqList.size(); i++) {
            FullFlowerRequest req = reqList.get(i);
            if (req.getId() == id) {
                return req;
            }
        }
        return null;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    public String toString() {
        return "FullFlowerRequest{" +
                "id=" + id +
                ", employee='" + employee + '\'' +
                ", floor='" + floor + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", dateSubmitted=" + dateSubmitted +
                ", requestStatus='" + requestStatus + '\'' +
                ", location_name='" + location_name + '\'' +
                ", flowerType='" + flowerType + '\'' +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", specialInstructions='" + specialInstructions + '\'' +
                '}';
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
