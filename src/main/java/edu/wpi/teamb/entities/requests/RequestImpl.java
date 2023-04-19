package edu.wpi.teamb.entities.requests;

import java.util.Date;

public abstract class RequestImpl implements IRequest {

  /**
   * Method to get the request ID
   *
   * @return requestID
   */
  public abstract int getRequestID();

  public abstract String getFloor();

  public abstract String getRoomNumber();

  public abstract Date getDateSubmitted();

  /**
   * Method to get the type of request
   *
   * @return RequestType
   */
  public abstract RequestType getRequestType();

  public abstract void submitRequest(String[] inputs);

  /**
   * Method to check if two requests are equal
   *
   * @param obj
   * @return true if equal, false if not
   */
  public abstract boolean equals(Object obj);

  /**
   * Method to get the hashcode of a request
   *
   * @return hashcode
   */
  public abstract int hashCode();
}
