package edu.wpi.teamb.controllers.components.popovers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ExitPopOverController {

    @FXML MFXButton btnYes;
    @FXML MFXButton btnNo;

    public void clickExit(){
        System.exit(0);
    }

    public void clickNo(){
        btnNo.getScene().getWindow().hide();
    }

}
