package edu.wpi.teamb.entities.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;

public class FlowerRequestE extends RequestImpl {
    private String employee;
    private String floor;
    private String roomNumber;
    private Date dateSubmitted;
    private String flowerType;
    private String color;
    private String type;
    private String message;
    private String specialInstructions;
    private RequestStatus requestStatus;

    public FlowerRequestE(String employee,
                          String floor,
                          String roomNumber,
                          Date dateSubmitted, String flowerType, String color, String type, String message,
                          String specialInstructions,
                          RequestStatus requestStatus) {
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.flowerType = flowerType;
        this.color = color;
        this.type = type;
        this.message = message;
        this.specialInstructions = specialInstructions;
        this.requestStatus = requestStatus;

    }

    public FlowerRequestE() {
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
        return RequestType.FlowerDelivery;
    }

    @Override
    public void submitRequest(String[] inputs) {
        String[] requestAttributes = {inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10], inputs[11]};
        //int id = Request.insertDBRowNewRequest(requestAttributes);
        //String[] flowerRequestAttributes = { String.valueOf(id), inputs[7], inputs[8], inputs[9] };
        //edu.wpi.teamb.DBAccess.ORMs.FlowerRequest.insertDBRowNewFlowerRequest(flowerRequestAttributes);
        Repository.getRepository().addFlowerRequest(requestAttributes);
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
        FlowerRequestE other = (FlowerRequestE) obj;
        return Objects.equals(color, other.color) && Objects.equals(dateSubmitted, other.dateSubmitted)
                && Objects.equals(employee, other.employee) && Objects.equals(floor, other.floor)
                && Objects.equals(flowerType, other.flowerType)
                && Objects.equals(roomNumber, other.roomNumber)
                && Objects.equals(requestStatus, other.requestStatus)
                && Objects.equals(specialInstructions, other.specialInstructions)
                && Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, dateSubmitted, employee, floor, flowerType, roomNumber, requestStatus,
                type, specialInstructions);
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