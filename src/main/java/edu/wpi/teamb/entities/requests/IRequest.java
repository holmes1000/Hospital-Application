package edu.wpi.teamb.entities.requests;

import java.sql.Timestamp;
import java.util.Date;

public interface IRequest {

  enum RequestType {
    MealDelivery,
    ConferenceRoom,
    FlowerDelivery,
    Move,
    OfficeSupplies,
    FurnitureDelivery
  }

  enum RequestStatus {
    Pending,
    Completed,
    Cancelled;
  }
}
