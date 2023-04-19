package edu.wpi.teamb.entities.requests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.ORMs.Request;

public class OfficeRequest extends RequestImpl {
    private String employee;
    private String floor;
    private String roomNumber;
    private Date dateSubmitted;
    private String item;
    private int quantity;
    private String specialInstructions;
    private String type;
    private RequestStatus requestStatus;

    public OfficeRequest(String employee,
                         String floor,
                         String roomNumber,
                         Date dateSubmitted, String item, int quantity, String specialInstructions, String type,
                         RequestStatus requestStatus) {
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.item = item;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.type = type;
        this.requestStatus = requestStatus;

    }

    public OfficeRequest() {
    }

    // getters and setters


    @Override
    public RequestType getRequestType() {
        return RequestType.OfficeSupplies;
    }

    @Override
    public void submitRequest(String[] inputs) {
        String[] requestAttributes = {inputs[0], inputs[1], inputs[2], inputs[3], inputs[4], inputs[5], inputs[6], inputs[7], inputs[8], inputs[9]};
        Repository.getRepository().addOfficeRequest(requestAttributes);
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
        OfficeRequest other = (OfficeRequest) obj;
        return Objects.equals(item, other.item) && Objects.equals(dateSubmitted, other.dateSubmitted)
                && Objects.equals(employee, other.employee) && Objects.equals(floor, other.floor)
                && Objects.equals(quantity, other.quantity)
                && Objects.equals(roomNumber, other.roomNumber)
                && Objects.equals(requestStatus, other.requestStatus)
                && Objects.equals(specialInstructions, other.specialInstructions)
                && Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, dateSubmitted, employee, floor, quantity, roomNumber, requestStatus,
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

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    /**
     * Method to get the request ID
     *
     * @return requestID
     */
    @Override
    public int getRequestID() {
        return 0;
    }

    @Override
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
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

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}