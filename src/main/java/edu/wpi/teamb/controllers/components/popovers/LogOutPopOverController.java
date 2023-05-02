package edu.wpi.teamb.controllers.components.popovers;

import edu.wpi.teamb.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class LogOutPopOverController {

    @FXML MFXButton btnYes;
    @FXML MFXButton btnNo;

    public void clickExit(){
        Navigation.navigate(edu.wpi.teamb.navigation.Screen.LOGIN);
    }

    public void clickNo(){
        btnNo.getScene().getWindow().hide();
    }

}
