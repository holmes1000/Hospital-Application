package edu.wpi.teamb.controllers.settings;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class EditUserController {

    @FXML private MFXTextField tfUsername;
    @FXML private MFXTextField tfName;
    @FXML private MFXTextField tfEmail;
    @FXML private MFXFilterComboBox<String> cbPermissionLevel;
    @FXML private MFXButton btnSaveEdits;
    static User currentUser = null;
    @FXML
    public void initialize() throws IOException {
        initializeFields();
        initButtons();
    }

    public void initializeFields() {
        // Initialize the user data
        tfName.setText(currentUser.getName());
        tfUsername.setText(currentUser.getUsername());
        tfUsername.setEditable(false); // cannot change username
        tfEmail.setText(currentUser.getEmail());
        // Init combo box
        ObservableList<String> permissionLevels = FXCollections.observableArrayList();

        permissionLevels.add("ADMIN");
        permissionLevels.add("EMPLOYEE");
        cbPermissionLevel.setItems(permissionLevels);
        cbPermissionLevel.selectItem(permissionLevelToString(currentUser.getPermissionLevel()));
        cbPermissionLevel.setText(permissionLevelToString(currentUser.getPermissionLevel()));

        // Sort the combo boxes
        Collections.sort(permissionLevels);

    }

    public void initButtons() {
        btnSaveEdits.setTooltip(new Tooltip("Click to save edits"));
        btnSaveEdits.setDisable(true);
        tfEmail.setTooltip(new Tooltip("Click to edit the email address"));
        tfUsername.setTooltip(new Tooltip("Username cannot be changed"));
        tfName.setTooltip(new Tooltip("Click to edit the name of the user"));
        cbPermissionLevel.setTooltip(new Tooltip("Click the dropdown to edit the permission level of the user"));
        btnSaveEdits.setOnMouseClicked(event -> handleSaveEdits());
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            btnSaveEdits.setDisable(false);
        };
        tfName.textProperty().addListener(changeListener);
        tfUsername.textProperty().addListener(changeListener);
        tfEmail.textProperty().addListener(changeListener);
        cbPermissionLevel.valueProperty().addListener(changeListener);
    }

    private void handleSaveEdits() {
        currentUser.setName(tfName.getText());
        currentUser.setUsername(tfUsername.getText());
        currentUser.setEmail(tfEmail.getText());
        currentUser.setPermissionLevel(permissionLevelToInt(cbPermissionLevel.getValue()));
        Repository.getRepository().updateUser(currentUser);

        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Updated Account Details");
        alert.setHeaderText(null);
        alert.setContentText("Successfully Updated Account Details");
        alert.showAndWait();
        Stage stage = (Stage) btnSaveEdits.getScene().getWindow();
        stage.close();
    }

    public static void setCurrentUser(User currentUser) {
        EditUserController.currentUser = currentUser;
    }

    private int permissionLevelToInt(String permissionLevel) {
        if (permissionLevel.equals("ADMIN")) {
            return 0;
        }
        if (permissionLevel.equals("EMPLOYEE")) {
            return 1;
        }
        else
            return 2; // Error
    }

    private String permissionLevelToString(int permissionLevel) {
        if (permissionLevel == 0) {
            return "ADMIN";
        }
        if (permissionLevel == 1) {
            return "EMPLOYEE";
        }
        else
            return "EMPLOYEE"; // Error
    }
}
