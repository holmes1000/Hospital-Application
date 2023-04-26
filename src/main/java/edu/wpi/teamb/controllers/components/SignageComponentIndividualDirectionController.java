package edu.wpi.teamb.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class SignageComponentIndividualDirectionController {
    @FXML Text signageDirectionIconLeft;
    @FXML Text signageDirectionIconRight;
    @FXML Text signageLocationText;


    public void initialize() throws IOException, SQLException {
    }

    /**
     * sets both signageDirectionIconLeft and signageDirectionIconRight to the same text
     */
    public void setSignageDirectionIcons(String text) {
        signageDirectionIconLeft.setText(text);
        signageDirectionIconRight.setText(text);
    }

    /**
     * sets signageLocationText to the given text
     * @param text
     */
    public void setSignageLocationText(String text) {
        signageLocationText.setText(text);
    }
}
