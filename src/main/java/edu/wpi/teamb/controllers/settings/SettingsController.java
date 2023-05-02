package edu.wpi.teamb.controllers.settings;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.ELogin;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingsController {
    @FXML
    private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    ELogin.PermissionLevel adminTest;
    @FXML private MFXButton btnEditUsers;
    @FXML private MFXButton btnEditAccount;
    @FXML private MFXButton btnEditAlerts;
    @FXML private MFXButton btnChangeServer;
    @FXML private MFXButton btnViewCSVs;
    @FXML private Pane navPane;
    @FXML private VBox vboxActivateNav;
    @FXML private VBox vboxActivateNav1;
    private boolean navLoaded;

    @FXML
    public void initialize() throws IOException {
        initNavBar();
        initButtons();
        adminTest = ELogin.getLogin().getPermissionLevel();
        if (adminTest != ELogin.PermissionLevel.ADMIN) {
            btnEditUsers.setVisible(false);
            btnEditAlerts.setVisible(false);
            btnChangeServer.setVisible(false);
            btnViewCSVs.setVisible(false);
        }
    }

    public void initButtons() {
        btnEditAlerts.setTooltip(new Tooltip("Edit the alerts that are sent to users"));
        btnEditAccount.setTooltip(new Tooltip("Edit your account information"));
        btnEditUsers.setTooltip(new Tooltip("Edit the users in the database"));
        btnChangeServer.setTooltip(new Tooltip("Change the database server"));
        btnViewCSVs.setTooltip(new Tooltip("View the CSVs of the database"));
        
        btnEditAlerts.setOnMouseClicked(event -> Navigation.navigate(Screen.EDIT_ALERTS));
        btnEditAccount.setOnMouseClicked(event -> Navigation.navigate(Screen.EDIT_ACCOUNT));
        btnEditUsers.setOnMouseClicked(event -> Navigation.navigate(Screen.EDIT_USERS));
        btnChangeServer.setOnMouseClicked(event -> changeServer());
        btnViewCSVs.setOnMouseClicked(event -> Navigation.navigate(Screen.VIEW_CSVS));
    }

    private void changeServer() {
        int server = Repository.getRepository().getDatabaseServer();
        if (server == 0) { // if server is WPI
            Repository.getRepository().switchTo(1);       // Swap to AWS
        } else if (server == 1){    // if server is AWS
            Repository.getRepository().switchTo(0);     // Else swap to WPI
        }
        server = Repository.getRepository().getDatabaseServer();
        if (server == 1) System.out.println("Server changed to AWS Postgres");
        else if (server == 0) System.out.println("Server changed to WPI Postgres");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Server changed!");
        alert.setHeaderText(null);
        if (server == 1) alert.setContentText("Successfully changed server to AWS Postgres");
        else if (server == 0) alert.setContentText("Successfully changed server to WPI Postgres");
        alert.showAndWait();
    }


    /**
     * Utilizes a gate to swap between handling the navdrawer and the rest of the page
     * Swaps ownership of the strip to the navdraw
     */

    public void activateNav(){
        vboxActivateNav.setOnMouseEntered(event -> {
            if(!navLoaded) {
                System.out.println("on");
                navPane.setMouseTransparent(false);
                navLoaded = true;
                vboxActivateNav.setDisable(true);
                vboxActivateNav1.setDisable(false);
            }
        });
    }

    /**
     * Utilizes a gate to swap between handling the navdrawer and the rest of the page
     * Swaps ownership of the strip to the page
     */
    public void deactivateNav(){
        vboxActivateNav1.setOnMouseEntered(event -> {
            if(navLoaded){
                System.out.println("off");
                navPane.setMouseTransparent(true);
                vboxActivateNav.setDisable(false);
                navLoaded = false;
                vboxActivateNav1.setDisable(true);
            }
        });
    }

    /**
     * Utilizes a gate to swap between handling the navdrawer and the rest of the page
     * Swaps ownership of the strip to the navdraw
     */
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
                        vboxActivateNav1.toFront();
                    } else {
                        menuDrawer.toFront();
                        menuBurger.toFront();
                        menuDrawer.open();
                    }
                });
    }
}
