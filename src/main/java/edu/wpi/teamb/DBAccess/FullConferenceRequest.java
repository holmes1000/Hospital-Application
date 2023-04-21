package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullConferenceRequest {
    int id;
    String employee;
    String floor;
    String roomNumber;
    Date dateSubmitted;
    String requestStatus;
    String location_name;
    Timestamp dateRequested;
    String eventName;
    String bookingReason;
    String requestType = "Conference";

    public FullConferenceRequest(Request request, ConferenceRequest conferenceRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.floor = request.getFloor();
        this.roomNumber = request.getRoomNumber();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.location_name = request.getLocationName();
        this.dateRequested = conferenceRequest.getDateRequested();
        this.eventName = conferenceRequest.getEventName();
        this.bookingReason = conferenceRequest.getBookingReason();
    }

    public FullConferenceRequest(int id, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus, String location_name, Timestamp dateRequested, String eventName, String bookingReason) {
        this.id = id;
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.location_name = location_name;
        this.dateRequested = dateRequested;
        this.eventName = eventName;
        this.bookingReason = bookingReason;
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

    public Timestamp getDateRequested() {
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

    public static ArrayList<FullConferenceRequest> listFullConferenceRequests(ArrayList<ConferenceRequest> crs) {
        ArrayList<FullConferenceRequest> fcrs = new ArrayList<FullConferenceRequest>();
        for (ConferenceRequest cr : crs) {
            Request r = RequestDAOImpl.getRequest(cr.getId());
            FullConferenceRequest fcr = new FullConferenceRequest(r, cr);
            fcrs.add(fcr);
        }
        return fcrs;
    }
    public static FullConferenceRequest getFullConferenceRequest(int id, List<FullConferenceRequest> reqList) {
        for (int i = 0; i < reqList.size(); i++) {
            FullConferenceRequest req = reqList.get(i);
            if (req.getId() == id) {
                return req;
            }
        }
        return null;
    }
    public void updateAll(List<FullConferenceRequest> reqList) {
        getFullConferenceRequest(getId(), reqList).setEmployee(getEmployee());
        getFullConferenceRequest(getId(), reqList).setFloor(getFloor());
        getFullConferenceRequest(getId(), reqList).setRoomNumber(getRoomNumber());
        getFullConferenceRequest(getId(), reqList).setDateSubmitted(getDateSubmitted());
        getFullConferenceRequest(getId(), reqList).setRequestStatus(getRequestStatus());
        getFullConferenceRequest(getId(), reqList).setLocation_name(getLocation_name());
        getFullConferenceRequest(getId(), reqList).setDateRequested(getDateRequested());
        getFullConferenceRequest(getId(), reqList).setEventName(getEventName());
        getFullConferenceRequest(getId(), reqList).setBookingReason(getBookingReason());
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    public String toString() {
        return "FullConferenceRequest{" +
                "id=" + id +
                ", employee='" + employee + '\'' +
                ", floor='" + floor + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", dateSubmitted=" + dateSubmitted +
                ", requestStatus='" + requestStatus + '\'' +
                ", location_name='" + location_name + '\'' +
                ", dateRequested=" + dateRequested +
                ", eventName='" + eventName + '\'' +
                ", bookingReason='" + bookingReason + '\'' +
                '}';
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
