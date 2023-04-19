package edu.wpi.teamb.entities.requests;

import java.util.Date;

public interface IRequest {
  /**
   * Returns the ID of the request
   *
   * @return the ID of the request
   */
  public default int getRequestID() {
    return 0;
  }

  /**
   * Returns the floor of where the request is being made for
   *
   * @return
   */
  public default String getFloor() {
    return "Not yet implemented";
  }

  /**
   * Returns the room number of where the request is being made for
   *
   * @return room number string
   */
  public default String getRoomNumber() {
    return "Not yet implemented";
  }

  /**
   * Returns the date the request was submitted
   *
   * @return date submitted
   */
  public default Date getDateSubmitted() {
    return new Date();
  }

  static enum RequestType {
    MealDelivery,
    ConferenceRoom,
    FlowerDelivery,
    Move,
    OfficeSupplies,
    FurnitureDelivery
  }

  static enum RequestStatus {
    Pending,
    InProgress,
    Completed,
    Cancelled;
  }
}
