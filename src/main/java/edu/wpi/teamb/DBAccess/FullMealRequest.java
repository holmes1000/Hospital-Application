package edu.wpi.teamb.DBAccess;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class FullMealRequest {
    int id;
    String employee;
    String floor;
    String roomNumber;
    Date dateSubmitted;
    String requestStatus;
    String locationName;
    String orderFrom;
    String food;
    String drink;
    String snack;
    String mealModification;
    String requestType = "Meal";

    public FullMealRequest(int id, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus, String location_name, String orderFrom, String food, String drink, String snack, String mealModification) {
        this.id = id;
        this.employee = employee;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = location_name;
        this.orderFrom = orderFrom;
        this.food = food;
        this.drink = drink;
        this.snack = snack;
        this.mealModification = mealModification;
    }

    public FullMealRequest(Request request, MealRequest mealRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.floor = request.getFloor();
        this.roomNumber = request.getRoomNumber();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.orderFrom = mealRequest.getOrderFrom();
        this.food = mealRequest.getFood();
        this.drink = mealRequest.getDrink();
        this.snack = mealRequest.getSnack();
        this.mealModification = mealRequest.getMealModification();
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

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
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

    public String getMealModification() {
        return mealModification;
    }

    public void setMealModification(String mealModification) {
        this.mealModification = mealModification;
    }
    public static ArrayList<FullMealRequest> listFullMealRequests(List<MealRequest> mrs) {
        ArrayList<FullMealRequest> fmrs = new ArrayList<FullMealRequest>();
        for (int i = 0; i < mrs.size(); i++) {
            MealRequest mr = mrs.get(i);
            Request r = RequestDAOImpl.getRequest(mr.getId());
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

    @Override
    public String toString() {
        return "FullMealRequest{" +
                "id=" + id +
                ", employee='" + employee + '\'' +
                ", floor='" + floor + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", dateSubmitted=" + dateSubmitted +
                ", requestStatus='" + requestStatus + '\'' +
                ", location_name='" + locationName + '\'' +
                ", orderFrom='" + orderFrom + '\'' +
                ", food='" + food + '\'' +
                ", drink='" + drink + '\'' +
                ", snack='" + snack + '\'' +
                ", mealModification='" + mealModification + '\'' +
                '}';
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
