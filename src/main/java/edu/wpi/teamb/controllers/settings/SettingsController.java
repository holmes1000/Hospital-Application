package edu.wpi.teamb.controllers.settings;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.LoginE;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SettingsController {
    @FXML
    private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    LoginE.PermissionLevel adminTest;
    @FXML private MFXButton btnEditUsers;

    @FXML
    public void initialize() throws IOException {
        initNavBar();
        adminTest = LoginE.getLogin().getPermissionLevel();
        if (adminTest != LoginE.PermissionLevel.ADMIN) {
            btnEditUsers.setVisible(false);
        }
    }

    @FXML
    public void clickEditUsers() {
        btnEditUsers.setOnMouseClicked(event -> Navigation.navigate(Screen.EDIT_USERS));
    }

    public void initNavBar() {
        // https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/NavDrawer.fxml"));
            VBox vbox = loader.load();
            NavDrawerController navDrawerController = loader.getController();
            menuDrawer.setSidePane(vbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition burgerOpen =
                new HamburgerBackArrowBasicTransition(menuBurger);
        burgerOpen.setRate(-1);
        menuBurger.addEventHandler(
                javafx.scene.input.MouseEvent.MOUSE_PRESSED,
                (e) -> {
                    burgerOpen.setRate(burgerOpen.getRate() * -1);
                    burgerOpen.play();
                    if (menuDrawer.isOpened()) {
                        menuDrawer.close();
                    } else {
                        menuDrawer.open();
                    }
                });
    }
}
