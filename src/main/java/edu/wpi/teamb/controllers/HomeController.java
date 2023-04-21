package edu.wpi.teamb.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomeController {
    @FXML private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private Pane navPane;
    @FXML private Pane homePane;
    @FXML private MFXButton btnAbout;
    @FXML private MFXButton btnCredits;
    private MFXButton pathfinderImgBtn = new MFXButton();
    @FXML
    public void initialize() throws IOException {
        initNavBar();
        initPathfinderBtn();
        initializeBtns();
    }

    private void initPathfinderBtn() {
        pathfinderImgBtn.setOnMouseClicked(e -> onPathfinderImgClick());
        Image mapImg = Bapp.getHospitalListOfFloors().get(0);
        ImageView imageView = new ImageView(mapImg);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        pathfinderImgBtn.setPrefSize(400, 400);
        pathfinderImgBtn.setLayoutX(400);
        pathfinderImgBtn.setLayoutY(100);
        pathfinderImgBtn.setGraphic(imageView);
        homePane.getChildren().add(pathfinderImgBtn);
    }

    private void initializeBtns() {
        btnCredits.setOnMouseClicked(e -> handleCredits());
        btnAbout.setOnMouseClicked(e -> handleAbout());
    }

    private void handleAbout() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/AboutPage.fxml")));
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCredits() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/CreditsPage.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Credits");
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void onPathfinderImgClick() {
        Navigation.navigate(Screen.PATHFINDER);
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
