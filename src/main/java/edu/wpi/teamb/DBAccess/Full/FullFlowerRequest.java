package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullFlowerRequest implements IFull {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String flowerType;
    String color;
    String size;
    String message;
    String requestType = "Flower";

    public FullFlowerRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, String flowerType, String color, String size, String message) {
        this.flowerType = flowerType;
        this.color = color;
        this.size = size;
        this.message = message;
    }
    public FullFlowerRequest(Request r, FlowerRequest f) {
        this.flowerType = f.getFlowerType();
        this.color = f.getColor();
        this.size = f.getSize();
        this.message = f.getMessage();
    }

    public FullFlowerRequest() {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    @Override
    public ArrayList<?> listFullRequests(List<?> frs) {
        ArrayList<FullFlowerRequest> ffrs = new ArrayList<FullFlowerRequest>();
        for (int i = 0; i < frs.size(); i++) {
            FlowerRequest fr = (FlowerRequest) frs.get(i);
            Request r = Request.getRequest(fr.getId());
            FullFlowerRequest ffr = new FullFlowerRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Flower Request");
    }

//    public static FullFlowerRequest getFullFlowerRequest(int id, List<FullFlowerRequest> reqList) {
//        for (int i = 0; i < reqList.size(); i++) {
//            FullFlowerRequest req = reqList.get(i);
//            if (req.getId() == id) {
//                return req;
//            }
//        }
//        return null;
//    }


}
