package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class RequestImpl implements IRequest {

  private String employee;
  private String locationName;
  private RequestStatus requestStatus;
  private String notes;

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

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }


  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public ArrayList<String> getUsernames() {
    ArrayList<String> employees = new ArrayList<>();
    ArrayList<User> users = Repository.getRepository().getAllUsers();
    for (User user : users) {
      employees.add(user.getUsername());
    }
    return employees;
  }

  public boolean checkRequestFields() {
    return this.employee != null && this.locationName != null && this.requestStatus != null;
  }

}
