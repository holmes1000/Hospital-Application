package edu.wpi.teamb.controllers.requests;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.sql.SQLException;

public interface IRequestController {

    /**
     * Initializes the buttons for the request form
     */
    void initBtns();

    /**
     * Initializes the fields for the request form
     */
    void initializeFields() throws SQLException;

    /**
     * Handles the submit button
     */
    void handleSubmit();

    /**
     * Handles the reset button
     */
    void handleReset();

    /**
     * Handles the cancel button
     */
    void handleCancel();

    /**
     * Handles the help button
     */
    void handleHelp();
}
