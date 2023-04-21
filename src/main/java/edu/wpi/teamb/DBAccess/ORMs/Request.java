package edu.wpi.teamb.DBAccess.ORMs;
import edu.wpi.teamb.DBAccess.DButils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Request {
  private int id;
  private String employee;
  private String floor;
  private String roomNumber;
  private java.sql.Date dateSubmitted;
  private String requestStatus;
  private String requestType;
  private String locationName;

  public Request() {
    this.id = 0;
    this.employee = "";
    this.floor = "";
    this.roomNumber = "";
    this.dateSubmitted = null;
    this.requestStatus = "";
    this.requestType = "";
    this.locationName = "";
  }

  public Request(int id, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus, String requestType, String locationName) {
    this.id = id;
    this.employee = employee;
    this.floor = floor;
    this.roomNumber = roomNumber;
    this.dateSubmitted = dateSubmitted;
    this.requestStatus = requestStatus;
    this.requestType = requestType;
    this.locationName = locationName;
  }

  public Request(ResultSet rs) throws java.sql.SQLException {
    this(
            rs.getInt("id"),
            rs.getString("employee"),
            rs.getString("floor"),
            rs.getString("roomnumber"),
            rs.getDate("datesubmitted"),
            rs.getString("requeststatus"),
            rs.getString("requesttype"),
            rs.getString("location_name")
    );
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

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  @Override
  public String toString() {
    return "Request ID: " + id + ",\t" +
            "employee: " + employee + ",\t" +
            "floor: " + floor + ",\t" +
            "Room Number: " + roomNumber + ",\t" +
            "Date Submitted: " + dateSubmitted + ",\t" +
            "Request Status: " + requestStatus + ",\t" +
            "Request Type: " + requestType + ".";
  }
}
