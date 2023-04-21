package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DButils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ConferenceRequest {
  private int id;
  private Timestamp dateRequested;
  private String eventName;
  private String bookingReason;

  public ConferenceRequest() {
    this.id = 0;
    this.dateRequested = null;
    this.eventName = "";
    this.bookingReason = "";
  }

  public ConferenceRequest(int id, Timestamp dateRequested, String eventName, String bookingReason) {
    this.id = id;
    this.dateRequested = dateRequested;
    this.eventName = eventName;
    this.bookingReason = bookingReason;
  }

  /**
   * Creates a new conference request object from a result set
   * @param rs the result set to create the conference request from
   */
  public ConferenceRequest(ResultSet rs) throws java.sql.SQLException {
    this(
      rs.getInt("id"),
      rs.getTimestamp("dateRequested"),
      rs.getString("eventName"),
      rs.getString("bookingReason")
    );
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
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

}
