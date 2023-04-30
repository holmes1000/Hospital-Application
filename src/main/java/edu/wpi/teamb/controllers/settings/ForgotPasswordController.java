package edu.wpi.teamb.controllers.settings;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ForgotPasswordController {
    @FXML MFXButton btnSendPassword;
    @FXML Text message;
    @FXML
    TextField tfEmail;
    @FXML
    SplitPane spSendPassword;
    @FXML
    public void initialize() throws IOException {
        initializeBtns();
    }

    private void initializeBtns() {;
        spSendPassword.setTooltip(new Tooltip("Enter your email to receive a password"));
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(tfEmail.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (tfEmail.getText().isEmpty());
            }
        };
        btnSendPassword.disableProperty().bind(bb);

        btnSendPassword.setTooltip(new Tooltip("Click to send password to email "));
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
