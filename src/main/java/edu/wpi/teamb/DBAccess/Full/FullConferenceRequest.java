package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullConferenceRequest implements IFull {
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

    public FullConferenceRequest() {

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    @Override
    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @Override
    public String getRequestStatus() {
        return requestStatus;
    }

    @Override
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    @Override
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setRequestType() {
        this.requestType = "Conference";
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

    @Override
    public ArrayList<?> listFullRequests(List<?> crs) {
        ArrayList<FullConferenceRequest> fcrs = new ArrayList<FullConferenceRequest>();
        for (int i = 0; i < crs.size(); i++) {
            ConferenceRequest or = (ConferenceRequest) crs.get(i);
            Request r = Request.getRequest(or.getId());
            FullConferenceRequest ofr = new FullConferenceRequest(r, or);
            fcrs.add(ofr);
        }
        return fcrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Conference Request");
    }

    @Override
    public void handleEditRequestMenu() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("/edu/wpi/teamb/views/requests/ConferenceRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Conference Edit Request Menu");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public Image setRequestTypeIconImageView() {
        return new Image("/edu/wpi/teamb/img/phone-call.png");
    }

}
