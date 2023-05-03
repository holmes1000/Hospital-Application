package edu.wpi.teamb.Game.Contollers;

/**
 * Sample Skeleton for 'StartScn.fxml' Controller Class
 */

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.teamb.Game.Gapp;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class StartScnController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnExitGame"
    private MFXButton btnExitGame; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlay"
    private MFXButton btnPlay; // Value injected by FXMLLoader

    @FXML // fx:id="btnRules"
    private MFXButton btnRules; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnExitGame != null : "fx:id=\"btnExitGame\" was not injected: check your FXML file 'StartScn.fxml'.";
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'StartScn.fxml'.";
        assert btnRules != null : "fx:id=\"btnRules\" was not injected: check your FXML file 'StartScn.fxml'.";

    }

    
    @FXML
    public void playGame()
    {
        Gapp.changeScene("./views/Game/rsc/Screens/GameScn.fxml");
        //create a new thread for the game to run

        Gapp.runGame();

       
     
    }

    @FXML 
    public void closeGame()
    {
        ((Stage)btnPlay.getScene().getWindow()).close();
        
    }

    @FXML 
    public void showHTP()
    {
        //change scene to ./rsc/Screens/StartScn.fxml
            
            Gapp.changeScene("./views/Game/rsc/Screens/HowToPlay.fxml");

    
    }

}
