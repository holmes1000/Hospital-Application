package edu.wpi.teamb.controllers.signage;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Sign;
import edu.wpi.teamb.entities.ESignage;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class AddNewSignageFormController {
    @FXML MFXTextField signageNameBox;
    @FXML MFXTextField location1Box;
    @FXML MFXTextField location2Box;
    @FXML MFXTextField location3Box;
    @FXML MFXTextField location4Box;
    @FXML MFXTextField location5Box;
    @FXML MFXTextField location6Box;
    @FXML MFXFilterComboBox<String> direction1Box;
    @FXML MFXFilterComboBox<String> direction2Box;
    @FXML MFXFilterComboBox<String> direction3Box;
    @FXML MFXFilterComboBox<String> direction4Box;
    @FXML MFXFilterComboBox<String> direction5Box;
    @FXML MFXFilterComboBox<String> direction6Box;
    @FXML MFXFilterComboBox<String> signLocationBox;
    @FXML MFXDatePicker startDatePicker;
    @FXML MFXDatePicker endDatePicker;
    @FXML MFXButton btnSubmit;
    @FXML MFXButton btnClose;
    private static SignageController signageController;

    private ESignage signageE;

    public void initialize(){
        //Init
        signageE = new ESignage();
        btnClose.setTooltip(new Tooltip("Click to return"));
        btnClose.setOnMouseClicked(e -> handleClose());
        btnSubmit.setTooltip(new Tooltip("Click to submit the form and create the new sign"));
        btnSubmit.setOnMouseClicked(e -> handleSubmit());
        initalizeLongNamesComboBox();
        initialize_date_pickers();
        initialize_directions();
    }

    private void initalizeLongNamesComboBox() {
        ArrayList<String> signLocations = signageE.getLongNamesAlphabetical();
        ObservableList<String> signageGroupsObservableList = FXCollections.observableArrayList(signLocations);
        signLocationBox.setItems(signageGroupsObservableList);
        signLocationBox.getSelectionModel().selectItem("Info Node 19 Floor 2");
    }

    private void initialize_date_pickers(){
        startDatePicker.setValue(LocalDate.now());
        //endDatePicker.setValue(LocalDate.now());
    }

    private void initialize_directions(){
        ObservableList<String> directions = FXCollections.observableArrayList();
        directions.add("right");
        directions.add("left");
        directions.add("up");
        directions.add("down");
        directions.add("stop here");
        direction1Box.setItems(directions);
        direction2Box.setItems(directions);
        direction3Box.setItems(directions);
        direction4Box.setItems(directions);
        direction5Box.setItems(directions);
        direction6Box.setItems(directions);
    }

    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void handleSubmit(){

        if ((location1Box.getText().isEmpty() && direction1Box.getValue() != null) ||
                (!location1Box.getText().isEmpty() && direction1Box.getValue() == null) ||
                (location2Box.getText().isEmpty() && direction2Box.getValue() != null) ||
                (!location2Box.getText().isEmpty() && direction2Box.getValue() == null) ||
                (location3Box.getText().isEmpty() && direction3Box.getValue() != null) ||
                (!location3Box.getText().isEmpty() && direction3Box.getValue() == null) ||
                (location4Box.getText().isEmpty() && direction4Box.getValue() != null) ||
                (!location4Box.getText().isEmpty() && direction4Box.getValue() == null) ||
                (location5Box.getText().isEmpty() && direction5Box.getValue() != null) ||
                (!location5Box.getText().isEmpty() && direction5Box.getValue() == null) ||
                (location6Box.getText().isEmpty() && direction6Box.getValue() != null) ||
                (!location6Box.getText().isEmpty() && direction6Box.getValue() == null)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MISSING FIELDS");
            alert.setHeaderText(null);
            alert.setContentText("Location and Direction must be filled together");
        }

        if(signageNameBox.getText().isEmpty() || location1Box.getText().isEmpty() || direction1Box.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MISSING FIELDS");
            alert.setHeaderText(null);
            alert.setContentText("Signage Name, Location 1, and Direction 1 must be filled at least.");
            alert.showAndWait();
        } else {
            // Send to DB
            if (!location1Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            if (!location2Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            if (!location3Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            if (!location4Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            if (!location5Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            if (!location6Box.getText().isEmpty()) {
                if (endDatePicker.getValue() != null) signageE.addSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                else signageE.addSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
            }
            // Create an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Submission Successful");
            alert.setHeaderText(null);
            alert.setContentText("Successfully created new sign named '" + signageNameBox.getText() + "'!");
            alert.showAndWait();
            signageController.refresh();
            handleClose();
        }
    }

    void setSignageController(SignageController signageController) {
        AddNewSignageFormController.signageController = signageController;
    }
}
