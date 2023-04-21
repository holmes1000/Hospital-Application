package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.FurnitureRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullFurnitureRequest implements IFull {
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
        this.type = type;
        this.model = model;
        this.assembly = assembly;
    }

    public FullFurnitureRequest(Request request, FurnitureRequest furnitureRequest) {
        this.type = furnitureRequest.getType();
        this.model = furnitureRequest.getModel();
        this.assembly = furnitureRequest.isAssembly();
    }

    public FullFurnitureRequest() {

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

    @Override
    public ArrayList<?> listFullRequests(List<?> frs) {
        ArrayList<FullFurnitureRequest> ffrs = new ArrayList<FullFurnitureRequest>();
        for (int i = 0; i < frs.size(); i++) {
            FurnitureRequest fr = (FurnitureRequest) frs.get(i);
            Request r = Request.getRequest(fr.getId());
            FullFurnitureRequest ffr = new FullFurnitureRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

}
