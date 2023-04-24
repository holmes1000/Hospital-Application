package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    @Override
    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @Override
    public String getRequestStatus() {
        return requestStatus;
    }

    @Override
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    @Override
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setRequestType() {
        this.requestType = "Meal";
    }

    @Override
    public ArrayList<?> listFullRequests(List<?> mrs) {
        ArrayList<FullMealRequest> fmrs = new ArrayList<FullMealRequest>();
        for (int i = 0; i < mrs.size(); i++) {
            MealRequest mr = (MealRequest) mrs.get(i);
            Request r = RequestDAOImpl.getRequest(mr.getId());
            FullMealRequest fmr = new FullMealRequest(r, mr);
            fmrs.add(fmr);
        }
        return fmrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Meal Request");
    }

    @Override
    public void handleEditRequestMenu() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("/edu/wpi/teamb/views/requests/MealRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Meal Request Edit Request Menu");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image setRequestTypeIconImageView() {
        return new Image("/edu/wpi/teamb/img/breakfast.png");
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
