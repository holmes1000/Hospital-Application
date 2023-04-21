package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.ORMs.FurnitureRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullFurnitureRequest {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String type;
    String model;
    boolean assembly;
    String requestType = "Furniture";

    public FullFurnitureRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, String type, String model, boolean assembly) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.type = type;
        this.model = model;
        this.assembly = assembly;
    }

    public FullFurnitureRequest(Request request, FurnitureRequest furnitureRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.type = furnitureRequest.getType();
        this.model = furnitureRequest.getModel();
        this.assembly = furnitureRequest.isAssembly();
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAssembly() {
        return assembly;
    }

    public void setAssembly(boolean assembly) {
        this.assembly = assembly;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static ArrayList<FullFurnitureRequest> listFullFurnitureRequests(List<FurnitureRequest> frs) {
        ArrayList<FullFurnitureRequest> ffrs = new ArrayList<FullFurnitureRequest>();
        for (int i = 0; i < frs.size(); i++) {
            FurnitureRequest fr = frs.get(i);
            Request r = Request.getRequest(fr.getId());
            FullFurnitureRequest ffr = new FullFurnitureRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

}
