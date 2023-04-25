package edu.wpi.teamb.controllers.settings;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

public class EditAlertController {

    @FXML private MFXTextField tfTitle;
    @FXML private MFXTextField tfDescription;
    @FXML private MFXButton btnSaveEdits;
    static edu.wpi.teamb.DBAccess.ORMs.Alert currentAlert = null;
    @FXML
    public void initialize() throws IOException {
        initializeFields();
        initButtons();
    }

    public void initializeFields() {
        // Initialize the alert data
        tfTitle.setText(currentAlert.getTitle());
        tfDescription.setText(currentAlert.getDescription());
    }

    public void initButtons() {
        btnSaveEdits.setOnMouseClicked(event -> handleSaveEdits());
    }

    private void handleSaveEdits() {
        currentAlert.setTitle(tfTitle.getText());
        currentAlert.setDescription(tfDescription.getText());
        currentAlert.setCreated_at(new Timestamp(System.currentTimeMillis()));
        Repository.getRepository().updateAlert(currentAlert);

        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updated Alert Details");
        alert.setHeaderText(null);
        alert.setContentText("Successfully Updated Alert Details");
        alert.showAndWait();
        Stage stage = (Stage) btnSaveEdits.getScene().getWindow();
        stage.close();
    }

    public static void setCurrentAlert(edu.wpi.teamb.DBAccess.ORMs.Alert currentAlert) {
        EditAlertController.currentAlert = currentAlert;
    }


}
