package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class FurnitureRequestE extends RequestImpl {
    private String employee;
    private String floor;
    private String roomNumber;
    private Date dateSubmitted;
    private String furnitureType;
    private String model;
    private String assembly;
    private String message;
    private String specialInstructions;
    private RequestStatus requestStatus;

    public FurnitureRequestE(String employee,
                             String floor,
                             String roomNumber,
                             Date dateSubmitted, String furnitureType, String model, String assembly, String message,
                             String specialInstructions,
                             RequestStatus requestStatus) {
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.furnitureType = furnitureType;
        this.model = model;
        this.assembly = assembly;
        this.message = message;
        this.specialInstructions = specialInstructions;
        this.requestStatus = requestStatus;

    }

    public FurnitureRequestE() {
    }

    // getters and setters
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getRequestID() {
        return 0;
    }

    @Override
    public String getFloor() {
        return floor;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.FurnitureDelivery;
    }

    @Override
    public void submitRequest(String[] inputs) {
        String[] requestAttributes = {inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11]};
        //int id = Request.insertDBRowNewRequest(requestAttributes);
        //String[] flowerRequestAttributes = { String.valueOf(id), inputs[7], inputs[8], inputs[9] };
        //edu.wpi.teamb.DBAccess.ORMs.FlowerRequest.insertDBRowNewFlowerRequest(flowerRequestAttributes);
        Repository.getRepository().addFurnitureRequest(requestAttributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FurnitureRequestE other = (FurnitureRequestE) obj;
        return Objects.equals(model, other.model) && Objects.equals(dateSubmitted, other.dateSubmitted)
                && Objects.equals(employee, other.employee) && Objects.equals(floor, other.floor)
                && Objects.equals(furnitureType, other.furnitureType)
                && Objects.equals(roomNumber, other.roomNumber)
                && Objects.equals(requestStatus, other.requestStatus)
                && Objects.equals(specialInstructions, other.specialInstructions)
                && Objects.equals(assembly, other.assembly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, dateSubmitted, employee, floor, furnitureType, roomNumber, requestStatus,
                assembly, specialInstructions);
    }

    public ArrayList<String> getUsernames() throws SQLException {
        ResultSet usernames = DB.getCol("users", "username");
        ArrayList<String> uesrlist = new ArrayList<>();
        while (usernames.next()) {
            uesrlist.add(usernames.getString("username"));
        }
        usernames.close();
        return uesrlist;
    }
}