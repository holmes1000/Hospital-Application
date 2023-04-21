package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullMealRequest {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String orderFrom;
    String food;
    String drink;
    String snack;
    String requestType = "Meal";

    public FullMealRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, String orderFrom, String food, String drink, String snack) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.orderFrom = orderFrom;
        this.food = food;
        this.drink = drink;
        this.snack = snack;
    }

    public FullMealRequest(Request request, MealRequest mealRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.orderFrom = mealRequest.getOrderFrom();
        this.food = mealRequest.getFood();
        this.drink = mealRequest.getDrink();
        this.snack = mealRequest.getSnack();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static ArrayList<FullMealRequest> listFullMealRequests(List<MealRequest> mrs) {
        ArrayList<FullMealRequest> fmrs = new ArrayList<FullMealRequest>();
        for (int i = 0; i < mrs.size(); i++) {
            MealRequest mr = mrs.get(i);
            Request r = Request.getRequest(mr.getId());
            FullMealRequest fmr = new FullMealRequest(r, mr);
            fmrs.add(fmr);
        }
        return fmrs;
    }

    public static FullOfficeRequest getFullMealRequest(int id, List<FullOfficeRequest> reqList) {
        for (int i = 0; i < reqList.size(); i++) {
            FullOfficeRequest req = reqList.get(i);
            if (req.getId() == id) {
                return req;
            }
        }
        return null;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
