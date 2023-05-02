package edu.wpi.teamb.Game.Contollers;

/**
 * Sample Skeleton for 'finish.fxml' Controller Class
 */

import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.teamb.Game.Game;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class EndController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="peopleText"
    private Text peopleText; // Value injected by FXMLLoader

    @FXML // fx:id="timeText"
    private Text timeText; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert peopleText != null : "fx:id=\"peopleText\" was not injected: check your FXML file 'finish.fxml'.";
        assert timeText != null : "fx:id=\"timeText\" was not injected: check your FXML file 'finish.fxml'.";

        peopleText.setText(""+Game.getPlayer().getScore()+" People");
        timeText.setText(""+Game.timeController.gettotalTime()+" Secdonds");

    }

}
