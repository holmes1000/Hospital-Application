package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.ORMs.Alert;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


import java.io.IOException;
import java.sql.SQLException;

public class AlertCardController {
    @FXML Label contextLabel;
    @FXML Label titleLabel;

    private Alert alert;

    /**
     * sets both signageDirectionIconLeft and signageDirectionIconRight to the same text
     */
    public void setLabels(Alert alert) {
      titleLabel.setText(alert.getTitle());
      contextLabel.setText(alert.getDescription());
    }

}
