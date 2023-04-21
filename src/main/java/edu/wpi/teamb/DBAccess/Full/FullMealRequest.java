package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FullMealRequest implements IFull {
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
        this.orderFrom = orderFrom;
        this.food = food;
        this.drink = drink;
        this.snack = snack;
    }

    public FullMealRequest(Request request, MealRequest mealRequest) {
        this.orderFrom = mealRequest.getOrderFrom();
        this.food = mealRequest.getFood();
        this.drink = mealRequest.getDrink();
        this.snack = mealRequest.getSnack();
    }

    public FullMealRequest() {
        this.orderFrom = "";
        this.food = "";
        this.drink = "";
        this.snack = "";
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public ArrayList<?> listFullRequests(List<?> mrs) {
        ArrayList<FullMealRequest> fmrs = new ArrayList<FullMealRequest>();
        for (int i = 0; i < mrs.size(); i++) {
            MealRequest mr = (MealRequest) mrs.get(i);
            Request r = Request.getRequest(mr.getId());
            FullMealRequest fmr = new FullMealRequest(r, mr);
            fmrs.add(fmr);
        }
        return fmrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Meal Request");
    }

//    public static FullOfficeRequest getFullMealRequest(int id, List<FullOfficeRequest> reqList) {
//        for (int i = 0; i < reqList.size(); i++) {
//            FullOfficeRequest req = reqList.get(i);
//            if (req.getId() == id) {
//                return req;
//            }
//        }
//        return null;
//    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
