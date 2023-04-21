package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullConferenceRequest {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    Timestamp dateRequested;
    String eventName;
    String bookingReason;
    int duration;
    String requestType = "Conference";

    public FullConferenceRequest(Request request, ConferenceRequest conferenceRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.dateRequested = conferenceRequest.getDateRequested();
        this.eventName = conferenceRequest.getEventName();
        this.bookingReason = conferenceRequest.getBookingReason();
        this.duration = conferenceRequest.getDuration();
    }

    public FullConferenceRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, Timestamp dateRequested, String eventName, String bookingReason, int duration) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.dateRequested = dateRequested;
        this.eventName = eventName;
        this.bookingReason = bookingReason;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static ArrayList<FullConferenceRequest> listFullConferenceRequests(ArrayList<ConferenceRequest> crs) {
        ArrayList<FullConferenceRequest> fcrs = new ArrayList<FullConferenceRequest>();
        for (ConferenceRequest cr : crs) {
            Request r = Request.getRequest(cr.getId());
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
        getFullConferenceRequest(getId(), reqList).setDateSubmitted(getDateSubmitted());
        getFullConferenceRequest(getId(), reqList).setRequestStatus(getRequestStatus());
        getFullConferenceRequest(getId(), reqList).setLocationName(getLocationName());
        getFullConferenceRequest(getId(), reqList).setNotes(getNotes());
        getFullConferenceRequest(getId(), reqList).setDateRequested(getDateRequested());
        getFullConferenceRequest(getId(), reqList).setEventName(getEventName());
        getFullConferenceRequest(getId(), reqList).setBookingReason(getBookingReason());
        getFullConferenceRequest(getId(), reqList).setDuration(getDuration());
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
