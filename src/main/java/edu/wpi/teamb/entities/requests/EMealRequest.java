package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EMealRequest extends RequestImpl {
  private String orderFrom;
  private String food;
  private String drink;
  private String snack;

  Request request;
  edu.wpi.teamb.DBAccess.ORMs.MealRequest mealRequest;


  public EMealRequest(String orderFrom, String food, String drink, String snack, Request request, MealRequest mealRequest) {
    this.orderFrom = orderFrom;
    this.food = food;
    this.drink = drink;
    this.snack = snack;
    this.request = request;
    this.mealRequest = mealRequest;
  }

  public EMealRequest() {
  }

  @Override
  public RequestType getRequestType() {
    return RequestType.MealDelivery;
  }

  @Override
  public void submitRequest(String[] inputs) {
    Repository.getRepository().addMealRequest(inputs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderFrom, food, drink, snack);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EMealRequest that = (EMealRequest) o;
    return Objects.equals(orderFrom, that.orderFrom) && Objects.equals(food, that.food) && Objects.equals(drink, that.drink) && Objects.equals(snack, that.snack) && Objects.equals(request, that.request) && Objects.equals(mealRequest, that.mealRequest);
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

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public MealRequest getMealRequest() {
    return mealRequest;
  }

  public void setMealRequest(MealRequest mealRequest) {
    this.mealRequest = mealRequest;
  }

  public boolean checkSpecialRequestFields() {
    return this.orderFrom != null && this.food != null && this.drink != null && this.snack != null;
  }
}
