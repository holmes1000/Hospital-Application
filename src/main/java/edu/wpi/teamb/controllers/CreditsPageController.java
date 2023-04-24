package edu.wpi.teamb.controllers;

import com.sun.glass.ui.ClipboardAssistance;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class CreditsPageController {
    @FXML MFXButton btnClose;

    @FXML
    public void initialize() throws IOException {
        initializeBtns();
    }

    private void initializeBtns() {;
        btnClose.setOnMouseClicked(e -> handleClose());
    }

    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
