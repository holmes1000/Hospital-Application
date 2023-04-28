package edu.wpi.teamb.controllers.settings;

import edu.wpi.teamb.DBAccess.DAO.AlertDAOImpl;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class EditAlertController {

    @FXML private MFXTextField tfTitle;
    @FXML private MFXTextField tfDescription;
    @FXML private MFXFilterComboBox<String> cbEmployees;
    @FXML private MFXButton btnSaveEdits;
    static edu.wpi.teamb.DBAccess.ORMs.Alert currentAlert = null;
    static edu.wpi.teamb.DBAccess.ORMs.Alert staticAlert = null;
    @FXML
    public void initialize() throws IOException {
        initializeFields();
        initButtons();
    }

    public void initializeFields() {
        // Initialize the alert data
        tfTitle.setText(currentAlert.getTitle());
        tfDescription.setText(currentAlert.getDescription());
        ArrayList<User> users = Repository.getRepository().getAllUsers();
        ArrayList<String> usernames = new ArrayList<>();
        for(int i = 0; i < users.size(); i++){
            usernames.add(users.get(i).getUsername());
        }
        cbEmployees.getItems().addAll(usernames);
        cbEmployees.setValue(currentAlert.getEmployee());
        Repository.getRepository().deleteAlert(currentAlert);
    }

    public void initButtons() {
        btnSaveEdits.setOnMouseClicked(event -> handleSaveEdits());
    }

    private void handleSaveEdits() {
        currentAlert.setTitle(tfTitle.getText());
        currentAlert.setDescription(tfDescription.getText());
        currentAlert.setCreated_at(new Timestamp(System.currentTimeMillis()));
        if(cbEmployees.getValue().equals("")){
            currentAlert.setEmployee("unassigned");
        } else {
            currentAlert.setEmployee(cbEmployees.getValue());
        }
        Repository.getRepository().addAlert(currentAlert);

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
        staticAlert = currentAlert;
    }


}
