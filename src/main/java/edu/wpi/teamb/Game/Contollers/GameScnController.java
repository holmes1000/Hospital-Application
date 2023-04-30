/**
 * Sample Skeleton for 'GameScn.fxml' Controller Class
 */

package edu.wpi.teamb.Game.Contollers;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.teamb.Game.Game;
import edu.wpi.teamb.Game.Gapp;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;

public class GameScnController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Quit"
    private MFXButton Quit; // Value injected by FXMLLoader

    @FXML // fx:id="Ssubmit"
    private MFXButton Ssubmit; // Value injected by FXMLLoader

    @FXML // fx:id="moveLeft"
    private MFXButton moveLeft; // Value injected by FXMLLoader

    @FXML // fx:id="moveRight"
    private MFXButton moveRight; // Value injected by FXMLLoader

    @FXML // fx:id="main"
    private Group Main; // Value injected by FXMLLoader

    @FXML
    private Canvas TheCanvas;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert Quit != null : "fx:id=\"Quit\" was not injected: check your FXML file 'GameScn.fxml'.";
        assert Ssubmit != null : "fx:id=\"Ssubmit\" was not injected: check your FXML file 'GameScn.fxml'.";
        assert moveLeft != null : "fx:id=\"moveLeft\" was not injected: check your FXML file 'GameScn.fxml'.";
        assert moveRight != null : "fx:id=\"moveRight\" was not injected: check your FXML file 'GameScn.fxml'.";

    }

    @FXML
    public void Quit()
    {
        Gapp.changeScene("./rsc/Screens/StartScn.fxml");
        Gapp.endGame();
    }
    @FXML
    public void moveRight()
    {
        Game.getPlayer().changeSelectedNeed(1);
    }
    @FXML
    public void moveLeft()
    {
        Game.getPlayer().changeSelectedNeed(-1);
    }
    @FXML
    public void enter()
    {
        Game.getPlayer().submitNeed();
    }

}
