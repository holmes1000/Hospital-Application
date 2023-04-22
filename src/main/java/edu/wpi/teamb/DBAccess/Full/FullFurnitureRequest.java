package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.FurnitureRequest;
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

public class FullFurnitureRequest implements IFull {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String type;
    String model;
    boolean assembly;
    String requestType = "Furniture";

    public FullFurnitureRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, String type, String model, boolean assembly) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.type = type;
        this.model = model;
        this.assembly = assembly;
    }

    public FullFurnitureRequest(Request request, FurnitureRequest furnitureRequest) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.type = furnitureRequest.getType();
        this.model = furnitureRequest.getModel();
        this.assembly = furnitureRequest.isAssembly();
    }

    public FullFurnitureRequest() {

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAssembly() {
        return assembly;
    }

    public void setAssembly(boolean assembly) {
        this.assembly = assembly;
    }
    public boolean getAssembly() {
        return assembly;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public void setRequestType() {
        this.requestType = "Furniture";
    }

    @Override
    public ArrayList<?> listFullRequests(List<?> frs) {
        ArrayList<FullFurnitureRequest> ffrs = new ArrayList<FullFurnitureRequest>();
        for (int i = 0; i < frs.size(); i++) {
            FurnitureRequest fr = (FurnitureRequest) frs.get(i);
            Request r = RequestDAOImpl.getRequest(fr.getId());
            FullFurnitureRequest ffr = new FullFurnitureRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Furniture Request");
    }

    @Override
    public void handleEditRequestMenu() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("/edu/wpi/teamb/views/requests/FurnitureRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Furniture Request Edit Request Menu");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image setRequestTypeIconImageView() {
        return new Image("/edu/wpi/teamb/img/sofa.png");
    }

}
