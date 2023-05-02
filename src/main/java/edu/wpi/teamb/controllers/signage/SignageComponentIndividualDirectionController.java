package edu.wpi.teamb.controllers.signage;

import edu.wpi.teamb.Bapp;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class SignageComponentIndividualDirectionController {
    @FXML ImageView signageDirectionIconLeft;
    @FXML ImageView signageDirectionIconRight;
    @FXML Text signageLocationText;


    public void initialize() throws IOException, SQLException {
    }

    /**
     * sets both signageDirectionIconLeft and signageDirectionIconRight to the same text
     */
    public void setSignageDirectionIcons(String text) {
        String directionIcon = "";
        switch (text) {
            case "left":
                directionIcon = "leftArrow";
                break;
            case "right":
                directionIcon = "rightArrow";
                break;
            case "up":
                directionIcon = "upArrow";
                break;
            case "down":
                directionIcon = "downArrow";
                break;
            case "stop here":
                directionIcon = "stop";
                break;
            default:
                directionIcon = " ";
                break;
        }

        Image arrow = new Image(Objects.requireNonNull(Bapp.class.getResourceAsStream("img/SignArrows/"+ directionIcon + ".png")));
        signageDirectionIconLeft.setImage(arrow);
        signageDirectionIconRight.setImage(arrow);
    }

    /**
     * sets signageLocationText to the given text
     * @param text
     */
    public void setSignageLocationText(String text) {
        signageLocationText.setText(text);
    }
}
