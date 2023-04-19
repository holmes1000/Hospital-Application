package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Request;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MealRequest extends RequestImpl {
  private String employee;
  private String floor;
  private String roomNumber;
  private Date dateSubmitted; // includes time
  private String orderFrom;
  private String food;
  private String drink;
  private String snack;
  private String mealModification;
  private RequestStatus requestStatus;

  Request request;
  edu.wpi.teamb.DBAccess.ORMs.MealRequest mealRequest;


  public MealRequest(
      String employee,
      String floor,
      String roomNumber,
      Date dateSubmitted,
      String orderFrom,
      String food,
      String drink,
      String snack,
      String mealModification,
      RequestStatus requestStatus) {
    this.employee = employee;
    this.floor = floor;
    this.roomNumber = roomNumber;
    this.dateSubmitted = dateSubmitted;
    this.orderFrom = orderFrom;
    this.food = food;
    this.drink = drink;
    this.snack = snack;
    this.mealModification = mealModification;
    this.requestStatus = requestStatus;
  }

  public MealRequest() {}

  @Override
  public int getRequestID() {
    return 0;
  }

  @Override
  public String getFloor() {
    return floor;
  }

  @Override
  public String getRoomNumber() {
    return roomNumber;
  }

  @Override
  public Date getDateSubmitted() {
    return dateSubmitted;
  }

  @Override
  public RequestType getRequestType() {
    return RequestType.MealDelivery;
  }

  @Override
  public void submitRequest(String[] inputs) {
    String[] requestAttributesValues = {inputs[0],inputs[1],inputs[2],inputs[3],inputs[4], inputs[5], inputs[6], inputs[7], inputs[8], inputs[9], inputs[10]};
    //int id = Request.insertDBRowNewRequest(requestAttributesValues);
    //int finalID = Request.getFinalID("id");
    //String[] mealAttributesValues = {String.valueOf(id), inputs[5],inputs[6],inputs[7],inputs[8],inputs[9]};
    //edu.wpi.teamb.DBAccess.ORMs.MealRequest.insertDBRowNewMealRequest(mealAttributesValues);
    Repository.getRepository().addMealRequest(requestAttributesValues);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    MealRequest mealRequest = (MealRequest) obj;
    return Objects.equals(this.employee, mealRequest.employee)
        && Objects.equals(this.floor, mealRequest.floor)
        && Objects.equals(this.roomNumber, mealRequest.roomNumber)
        && Objects.equals(this.dateSubmitted, mealRequest.dateSubmitted)
        && Objects.equals(this.orderFrom, mealRequest.orderFrom)
        && Objects.equals(this.food, mealRequest.food)
        && Objects.equals(this.drink, mealRequest.drink)
        && Objects.equals(this.snack, mealRequest.snack)
        && Objects.equals(this.mealModification, mealRequest.mealModification)
        && Objects.equals(this.requestStatus, mealRequest.requestStatus);
  }

  public String getEmployee() {
    return employee;
  }

  public void setEmployee(String employee) {
    this.employee = employee;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public void setDateSubmitted(Date dateSubmitted) {
    this.dateSubmitted = dateSubmitted;
  }

  public String getOrderFrom() {
    return orderFrom;
  }

  public void setOrderFrom(String orderFrom) {
    this.orderFrom = orderFrom;
  }

  public String getFood() {
    return food;
  }

  public void setFood(String food) {
    this.food = food;
  }

  public String getDrink() {
    return drink;
  }

  public void setDrink(String drink) {
    this.drink = drink;
  }

  public String getSnack() {
    return snack;
  }

  public void setSnack(String snack) {
    this.snack = snack;
  }

  public String getMealModification() {
    return mealModification;
  }

  public void setMealModification(String mealModification) {
    this.mealModification = mealModification;
  }

  public RequestStatus getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    this.requestStatus = requestStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        employee,
        floor,
        roomNumber,
        dateSubmitted,
        orderFrom,
        food,
        drink,
        snack,
        mealModification,
        requestStatus);
  }

  public ArrayList<String> getUsernames() throws SQLException {
    ResultSet usernames = DB.getCol("users", "username");
    ArrayList<String> userList = new ArrayList<String>();
            while(usernames.next()){
              userList.add(usernames.getString("username"));

            }
            usernames.close();
    return userList;
  }



}
