package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;
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

public class FullOfficeRequest implements IFull {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String type;
    String item;
    int quantity;
    String requestType = "Office";

    public FullOfficeRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String type, String notes, String item, int quantity) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.item = item;
        this.quantity = quantity;
        this.type = type;
    }

    public FullOfficeRequest(Request request, OfficeRequest officeRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.type = officeRequest.getType();
        this.item = officeRequest.getItem();
        this.quantity = officeRequest.getQuantity();
    }

    public FullOfficeRequest() {
    }

    @Override
    public ArrayList<?> listFullRequests(List<?> list) {
        ArrayList<FullOfficeRequest> fors = new ArrayList<FullOfficeRequest>();
        for (int i = 0; i < list.size(); i++) {
            OfficeRequest or = (OfficeRequest) list.get(i);
            Request r = RequestDAOImpl.getRequest(or.getId());
            FullOfficeRequest ofr = new FullOfficeRequest(r, or);
            fors.add(ofr);
        }
        return fors;
    }

    @Override
    public void printRequestType() {
        System.out.println("Office Request");
    }

    @Override
    public void handleEditRequestMenu() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("/edu/wpi/teamb/views/requests/OfficeRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Office Supplies Edit Request Menu");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image setRequestTypeIconImageView() {
        return new Image("/edu/wpi/teamb/img/workspace.png");
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
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setRequestType() {
        this.requestType = "Office";
    }


//    public static FullOfficeRequest getFullOfficeRequest(int id, List<FullOfficeRequest> reqList) {
//        for (int i = 0; i < reqList.size(); i++) {
//            FullOfficeRequest req = reqList.get(i);
//            if (req.getId() == id) {
//                return req;
//            }
//        }
//        return null;
//    }

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
