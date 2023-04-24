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
  private int duration;

  public ConferenceRequest() {
    this.id = 0;
    this.dateRequested = null;
    this.eventName = "";
    this.bookingReason = "";
    this.duration = 0;
  }

  public ConferenceRequest(int id, Timestamp dateRequested, String eventName, String bookingReason, int duration) {
    this.id = id;
    this.dateRequested = dateRequested;
    this.eventName = eventName;
    this.bookingReason = bookingReason;
    this.duration = duration;
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
      rs.getString("bookingReason"),
      rs.getInt("duration")
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
  public int getDuration() {
    return duration;
  }
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Searches through the database for the row(s) that matches the given request ID
   *
   * @param id the request ID to search for
   * @return the result set of the row(s) that matches the given column and value
   */
  public static ConferenceRequest getConfRequest(int id) {
    ResultSet rs = DButils.getRowCond("ConferenceRequests", "*", "id = '" + id + "'");
    try {
      assert rs != null;
      if (rs.isBeforeFirst()) {
        rs.next();
        return new ConferenceRequest(rs);
      }else
        throw new SQLException("No rows found");
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
