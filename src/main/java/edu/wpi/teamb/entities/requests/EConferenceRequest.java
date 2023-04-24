package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EConferenceRequest extends RequestImpl {
  private Timestamp dateRequested = null;
  private String eventName = null;
  private String bookingReason = null;
  private int duration = 0;

    public EConferenceRequest(String dateRequested, String eventName, String bookingReason, int duration) {
        this.dateRequested = Timestamp.valueOf(dateRequested);
        this.eventName = eventName;
        this.bookingReason = bookingReason;
        this.duration = duration;
    }

  public EConferenceRequest() {}

  @Override
  public RequestType getRequestType() {
    return RequestType.ConferenceRoom;
  }

  @Override
  public void submitRequest(String[] inputs) {
    Repository.getRepository().addConferenceRequest(inputs);
  }

  @Override
  public boolean equals(Object obj) {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
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

  public boolean checkSpecialRequestFields() {
    return this.dateRequested != null && this.eventName != null && this.bookingReason != null && this.duration != 0;
  }

  public void updateConferenceRequest(FullConferenceRequest fullConferenceRequest) {
    Repository.getRepository().updateConferenceRequest(fullConferenceRequest);
  }

}
