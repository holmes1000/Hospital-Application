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

public class EditSignageFormController {
    @FXML
    MFXTextField signageNameBox;
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
    private String originalLocation1;
    private String originalLocation2;
    private String originalLocation3;
    private String originalLocation4;
    private String originalLocation5;
    private String originalLocation6;

    private static String curSignage;

    private ESignage signageE;

    public void initialize(){
        //Init
        signageE = new ESignage();
        curSignage = ESignage.getCurrentSignageGroup();
        btnClose.setTooltip(new Tooltip("Click to return"));
        btnClose.setOnMouseClicked(e -> handleClose());
        btnSubmit.setTooltip(new Tooltip("Click to submit edits to the current sign"));
        btnSubmit.setOnMouseClicked(e -> handleSubmit());
        initializeSignName();
        initialize_directions();
        initialize_date_pickers();
        initializeSignLocationsAndDirections();
        initalizeLongNamesComboBox();

    }

    private void initializeSignName() {
        signageNameBox.setText(curSignage);
    }

    private void initializeSignLocationsAndDirections() {
        ArrayList<String> signLocations = signageE.getLocationNames(curSignage);
        ObservableList<String> locationsObservableList = FXCollections.observableArrayList(signLocations);
        for (int i = 0; i < signLocations.size(); i++) {
            switch (i) {
                case 0 -> location1Box.setText(locationsObservableList.get(i));
                case 1 -> location2Box.setText(locationsObservableList.get(i));
                case 2 -> location3Box.setText(locationsObservableList.get(i));
                case 3 -> location4Box.setText(locationsObservableList.get(i));
                case 4 -> location5Box.setText(locationsObservableList.get(i));
                case 5 -> location6Box.setText(locationsObservableList.get(i));
            }
        }
        originalLocation1 = location1Box.getText();
        originalLocation2 = location2Box.getText();
        originalLocation3 = location3Box.getText();
        originalLocation4 = location4Box.getText();
        originalLocation5 = location5Box.getText();
        originalLocation6 = location6Box.getText();
        ArrayList<String> directions = signageE.getDirectionsFromLocations(curSignage,signLocations);
        ObservableList<String> directionsObservableList = FXCollections.observableArrayList(directions);
        for (int i = 0; i < directions.size(); i++) {
            switch (i) {
                case 0 -> direction1Box.setValue(directions.get(i));
                case 1 -> direction2Box.setValue(directions.get(i));
                case 2 -> direction3Box.setValue(directions.get(i));
                case 3 -> direction4Box.setValue(directions.get(i));
                case 4 -> direction5Box.setValue(directions.get(i));
                case 5 -> direction6Box.setValue(directions.get(i));
            }
        }
    }

    private void initalizeLongNamesComboBox() {
        ArrayList<String> signLocations = signageE.getLongNamesAlphabetical();
        ObservableList<String> signageGroupsObservableList = FXCollections.observableArrayList(signLocations);
        signLocationBox.setItems(signageGroupsObservableList);
        signLocationBox.getSelectionModel().selectItem("Info Node 19 Floor 2");
    }

    private void initialize_date_pickers(){
        startDatePicker.setValue(signageE.getStartDate(curSignage, location1Box.getText()).toLocalDate());
        if (signageE.getEndDate(curSignage, location1Box.getText()) != null) {
        endDatePicker.setValue(signageE.getEndDate(curSignage, location1Box.getText()).toLocalDate());}
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
            alert.setTitle("MISSING FIELDS: Location and Direction must be filled together");
            alert.setHeaderText(null);
            alert.setContentText("Must include a location and corresponding direction together.");
        }

        if(signageNameBox.getText().isEmpty() || location1Box.getText().isEmpty() || direction1Box.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MISSING FIELDS: Sign name, Location 1 (minimum), and Direction 1 (minimum)");
            alert.setHeaderText(null);
            alert.setContentText("Must include a Sign name, one location, and one corresponding direction minimum.");
            alert.showAndWait();
        } else {
            // Send to DB
            if (!location1Box.getText().isEmpty()) {
                if (!originalLocation1.equals(location1Box.getText())) {
                    if (originalLocation1.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation1, new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation1, new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location1Box.getText(), direction1Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            if (!location2Box.getText().isEmpty()) {
                if (!originalLocation2.equals(location2Box.getText())) {
                    if (originalLocation2.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation2, new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation2, new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location2Box.getText(), direction2Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            if (!location3Box.getText().isEmpty()) {
                if (!originalLocation3.equals(location3Box.getText())) {
                    if (originalLocation3.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation3, new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation3, new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location3Box.getText(), direction3Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            if (!location4Box.getText().isEmpty()) {
                if (!originalLocation4.equals(location4Box.getText())) {
                    if (originalLocation4.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation4, new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation4, new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location4Box.getText(), direction4Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            if (!location5Box.getText().isEmpty()) {
                if (!originalLocation5.equals(location5Box.getText())) {
                    if (originalLocation5.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation5, new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation5, new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location5Box.getText(), direction5Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            if (!location6Box.getText().isEmpty()) {
                if (!originalLocation6.equals(location6Box.getText())) {
                    if (originalLocation6.isEmpty()) {
                        if (endDatePicker.getValue() != null)
                            signageE.addSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.addSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    } else {
                        if (endDatePicker.getValue() != null)
                            signageE.transferSign(originalLocation6, new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                        else
                            signageE.transferSign(originalLocation6, new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                    }
                } else {
                    if (endDatePicker.getValue() != null)
                        signageE.updateSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), Date.valueOf(endDatePicker.getValue()), signLocationBox.getValue()));
                    else
                        signageE.updateSign(new Sign(signageNameBox.getText(), location6Box.getText(), direction6Box.getValue(), Date.valueOf(startDatePicker.getValue()), null, signLocationBox.getValue()));
                }
            }
            // Create an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Submission Successful");
            alert.setHeaderText(null);
            alert.setContentText("Successfully edited sign named '" + signageNameBox.getText() + "'!");
            alert.showAndWait();
            //signageController.refresh();
            handleClose();
        }
    }

    void setSignageController(SignageController signageController) {
        EditSignageFormController.signageController = signageController;
    }

}
