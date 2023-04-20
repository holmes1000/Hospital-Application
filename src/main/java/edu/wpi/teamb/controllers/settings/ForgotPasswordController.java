package edu.wpi.teamb.controllers.settings;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {
    @FXML MFXButton btnSendPassword;
    @FXML Text message;
    @FXML
    public void initialize() throws IOException {
        initializeBtns();
    }

    private void initializeBtns() {;
        btnSendPassword.setOnMouseClicked(e -> {
            try {
                handleSendPassword();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void handleSendPassword() throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Sent!");
        alert.setHeaderText(null);
        alert.setContentText("Password sent.");
        alert.showAndWait();
        Stage stage = (Stage) btnSendPassword.getScene().getWindow();
        stage.close();
    }
}
