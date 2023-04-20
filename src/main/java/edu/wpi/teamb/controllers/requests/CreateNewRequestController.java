package edu.wpi.teamb.controllers.requests;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.ELogin;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CreateNewRequestController {
    @FXML
    MFXButton btnHome;
    @FXML
    private JFXHamburger menuBurger;
    @FXML
    private JFXDrawer menuDrawer;
    @FXML
    private MFXComboBox cbRequestType;
    @FXML
    private VBox requestVbox;
    @FXML
    MFXButton btnAllRequests;
    ELogin.PermissionLevel adminTest;
    private NavDrawerController navDrawerController;

    @FXML
    public void initialize() throws IOException {
        initNavBar();
        requestVbox.getChildren().clear();
        requestVbox.setFillWidth(true);
        adminTest = ELogin.getLogin().getPermissionLevel();
        initializeFields();
        if (adminTest != ELogin.PermissionLevel.ADMIN) {
            btnAllRequests.setVisible(false);
        }
        requestVbox.getChildren().clear();
        loadPage2();
    }

    public void clickCbRequestType() {
        requestVbox.getChildren().clear();
        displaySelection();
    }

    @FXML
    public void clickAllRequests() {
        btnAllRequests.setOnMouseClicked(event -> Navigation.navigate(Screen.ALL_REQUEST));
    }

    public void displaySelection() {
        String item = cbRequestType.getSelectedItem().toString();
        switch (item) {
            case "Meal Delivery" -> loadPage1();
            case "Conference Room" -> loadPage2();
            case "Flower Delivery" -> loadPage3();
            case "Move" -> loadPage4();
            case "Office Supplies" -> loadPage5();
            case "Furniture Delivery" -> loadPage6();
            default -> loadPage2();
        }
        ;
    }

    public void loadPage1() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/requests/MealRequest.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage2() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/requests/ConferenceRequest.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage3() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/requests/FlowerRequests.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadPage4() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/requests/MoveRequest.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage5() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/requests/OfficeRequest.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage6() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/FurnitureRequest.fxml"));
            Pane pane = loader.load();
            requestVbox.getChildren().addAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initializeFields() {

        ObservableList<String> locations = FXCollections.observableArrayList("Meal Delivery", "Conference Room",
                "Flower Delivery", "Furniture Delivery", "Office Supplies", "Other");
        // TODO: only add thise if the user is an admin
        if (adminTest == ELogin.PermissionLevel.ADMIN) {
            ObservableList<String> AdminOnly = FXCollections.observableArrayList("Move");
            for (String string : AdminOnly) {
                locations.add(locations.size() - 2, string);
            }
        }
        cbRequestType.setItems(locations);
    }

    public void initNavBar() {
        // https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/edu/wpi/teamb/views/components/NavDrawer.fxml"));
            VBox vbox = loader.load();
            NavDrawerController navDrawerController = loader.getController();
            menuDrawer.setSidePane(vbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition burgerOpen = new HamburgerBackArrowBasicTransition(menuBurger);
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
