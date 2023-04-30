package edu.wpi.teamb.controllers.settings;

import edu.wpi.teamb.entities.EForgotPassword;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordController {
    @FXML MFXButton btnSendPassword;
    @FXML Text message;
    @FXML TextField tfEmail;
    @FXML SplitPane spSendPassword;
    EForgotPassword forgotPasswordE;

    @FXML
    public void initialize() throws IOException {
        initializeBtns();
        forgotPasswordE = new EForgotPassword();
    }

    private void initializeBtns() {
        ;
        spSendPassword.setTooltip(new Tooltip("Enter your email to receive a password"));
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(tfEmail.textProperty());
            }
            @Override
            protected boolean computeValue() {
                String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$";
                Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(tfEmail.getText());
                return !matcher.matches();
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
        forgotPasswordE.sendForgotPasswordEmail(tfEmail.getText());
        alert.setContentText("Password sent.");
        alert.showAndWait();
        Stage stage = (Stage) btnSendPassword.getScene().getWindow();
        stage.close();
    }
}
