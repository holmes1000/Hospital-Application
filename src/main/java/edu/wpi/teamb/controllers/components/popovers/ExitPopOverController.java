package edu.wpi.teamb.controllers.components.popovers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class ExitPopOverController {

    @FXML MFXButton btnYes;
    @FXML MFXButton btnNo;

    public void clickExit(){
        btnYes.setOnMouseClicked(event -> System.exit(0));
    }

}
