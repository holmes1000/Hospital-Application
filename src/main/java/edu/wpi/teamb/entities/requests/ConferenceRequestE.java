package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ConferenceRequestE extends RequestImpl {
  private String employee;
  private String floor;
  private String roomNumber;
  private Date dateSubmitted;
  private Timestamp dateRequested; // includes time
  private String eventName;
  private String bookingReason;
  private RequestStatus requestStatus;
  private List<String> availableRooms;

  public ConferenceRequestE(
      String employee,
      String floor,
      String roomNumber,
      Date dateSubmitted,
      Timestamp dateRequested,
      String eventName,
      String bookingReason,
      List<String> availableRooms,
      RequestStatus requestStatus) {
    this.employee = employee;
    this.floor = floor;
    this.roomNumber = roomNumber;
    this.dateSubmitted = dateSubmitted;
    this.dateRequested = dateRequested;
    this.eventName = eventName;
    this.bookingReason = bookingReason;
    this.availableRooms = availableRooms;
    this.requestStatus = requestStatus;
  }

  public ConferenceRequestE() {}

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
    return RequestType.ConferenceRoom;
  }

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  @Override
  public void submitRequest(String[] inputs) {
    //String[] requestAttributesValues = {inputs[0],inputs[1],inputs[2], "'" + "2023-04-05" + "'",inputs[3],inputs[4]};
    String[] attributes = {inputs[0],inputs[1],inputs[2],inputs[3],inputs[4],inputs[5],inputs[6], inputs[7], inputs[8]};
    //int id = Request.insertDBRowNewRequest(requestAttributesValues);
    //int finalID = Request.getFinalID( "id");
    //String[] conferenceRequestAttributeValues = {String.valueOf(id), inputs[5],inputs[6],inputs[7]};
    Repository.getRepository().addConferenceRequest(inputs);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    ConferenceRequestE conferenceRequestE = (ConferenceRequestE) obj;
    return Objects.equals(employee, conferenceRequestE.employee)
        && Objects.equals(floor, conferenceRequestE.floor)
        && Objects.equals(roomNumber, conferenceRequestE.roomNumber)
        && Objects.equals(dateSubmitted, conferenceRequestE.dateSubmitted)
        && Objects.equals(dateRequested, conferenceRequestE.dateRequested)
        && Objects.equals(eventName, conferenceRequestE.eventName)
        && Objects.equals(bookingReason, conferenceRequestE.bookingReason)
        && Objects.equals(requestStatus, conferenceRequestE.requestStatus);
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

  public Date getDateRequested() {
    return dateRequested;
  }

  public void setDateRequested(Timestamp dateRequested) {
    this.dateRequested = dateRequested;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getBookingReason() {
    return bookingReason;
  }

  public void setBookingReason(String bookingReason) {
    this.bookingReason = bookingReason;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  public void setAvailableRooms(List<String> availableRooms) {
    this.availableRooms = availableRooms;
  }

  public List<String> getAvailableRooms() {
    return availableRooms;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        employee,
        floor,
        roomNumber,
        dateSubmitted,
        dateRequested,
        eventName,
        bookingReason,
        requestStatus,
        availableRooms);
  }

  public ArrayList<String> getUsernames() throws SQLException {
    ResultSet usernames = DB.getCol("users", "username");
    ArrayList<String> userList = new ArrayList<String>();
    while (usernames.next()) {
      userList.add(usernames.getString("username"));
    }
    usernames.close();
    return userList;
  }
}
