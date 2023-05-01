package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.DBAccess.ORMs.TranslationRequest;
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

public class FullTranslationRequest implements IFull {
    int id;
    String employee;
    Timestamp dateSubmitted;
    String requestStatus;
    String locationName;
    String notes;
    String languageType;
    String medical;
    String message;
    String requestType = "Translation";

    public FullTranslationRequest(int id, String employee, Timestamp dateSubmitted, String requestStatus, String locationName, String notes, String language, String medical, String message) {
        this.id = id;
        this.employee = employee;
        this.dateSubmitted = dateSubmitted;
        this.requestStatus = requestStatus;
        this.locationName = locationName;
        this.notes = notes;
        this.languageType = language;
        this.medical = medical;
        this.message = message;
    }
    public FullTranslationRequest(Request request, TranslationRequest f) {
        this.id = request.getId();
        this.employee = request.getEmployee();
        this.dateSubmitted = request.getDateSubmitted();
        this.requestStatus = request.getRequestStatus();
        this.locationName = request.getLocationName();
        this.notes = request.getNotes();
        this.languageType = f.getLanguage();
        this.medical = f.getMedical();
        this.message = f.getMessage();
    }

    public FullTranslationRequest() {
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

    public String getLanguage() {
        return languageType;
    }

    public void setLanguage(String flowerType) {
        this.languageType = flowerType;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String color) {
        this.medical = medical;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        this.requestType = "Translation";
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

    @Override
    public ArrayList<?> listFullRequests(List<?> frs) {
        ArrayList<FullTranslationRequest> ffrs = new ArrayList<FullTranslationRequest>();
        for (int i = 0; i < frs.size(); i++) {
            TranslationRequest fr = (TranslationRequest) frs.get(i);
            Request r = RequestDAOImpl.getRequest(fr.getId());
            FullTranslationRequest ffr = new FullTranslationRequest(r, fr);
            ffrs.add(ffr);
        }
        return ffrs;
    }

    @Override
    public void printRequestType() {
        System.out.println("Translation Request");
    }

    @Override
    public void handleEditRequestMenu() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("/edu/wpi/teamb/views/requests/TranslationRequest.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Language Edit Request Menu");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image setRequestTypeIconImageView() {
        return new Image("/edu/wpi/teamb/img/flower.png");
    }


    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }
}