package edu.wpi.teamb.Game.Contollers;
/**
 * Sample Skeleton for 'HowToPlay.fxml' Controller Class
 */

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

public class HowToPlayController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnBack"
    private MFXButton btnBack; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnBack != null : "fx:id=\"btnBack\" was not injected: check your FXML file 'HowToPlay.fxml'.";

    }

}
