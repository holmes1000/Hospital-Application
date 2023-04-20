package edu.wpi.teamb.controllers.requests;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.EPathfinder;
import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import static javafx.scene.paint.Color.*;

public class MoveMapController {
    @FXML private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private ImageView helpIcon;
    @FXML private VBox VboxPathfinder;
    @FXML private StackPane stackPaneMapView;
    @FXML private ImageView imageViewPathfinder;
    @FXML private MFXButton btnL1;
    @FXML private MFXButton btnL2;
    @FXML private MFXButton btn1;
    @FXML private MFXButton btn2;
    @FXML private MFXButton btn3;
    private String currentFloor = "1";

    public GesturePane pane = new GesturePane();
    private ArrayList<Move> allMoves;
    Group pathGroup;
    Pane locationCanvas;
    @FXML
    public void initialize() throws IOException {
        initNavBar();
        hoverHelp();
        initButtons();

        this.stackPaneMapView = new StackPane(); // no longer @FXML
        this.pathGroup = new Group();
        this.locationCanvas = new Pane();
        this.allMoves = Repository.getRepository().getAllMoves();

        this.pane.setContent(stackPaneMapView);
        this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML
        this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
        this.stackPaneMapView.getChildren().add(this.locationCanvas);

        this.locationCanvas.getChildren().add(pathGroup);

        pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
        pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        Platform.runLater(() -> this.pane.centreOn(new Point2D(2190, 910)));
        System.out.println("Move Map initialized");
    }

    private void changeButtonColor(String currentFloor) {
        switch (currentFloor) {
            case "L1" -> {
                btnL1.setStyle("-fx-background-color: #f6bd38");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "L2" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #f6bd38");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "1" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #f6bd38");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "2" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #f6bd38");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "3" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #f6bd38");
            }
        }
    }


    public void initButtons() {
        clickFloorBtn("L1");
        clickFloorBtn("L2");
        clickFloorBtn("1");
        clickFloorBtn("2");
        clickFloorBtn("3");
    }

    public void clickFloorBtn(String floor) {
        btnL1.setOnMouseClicked(event->{
            currentFloor = "L1";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(0));
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().add(pathGroup);
        });
        btnL2.setOnMouseClicked(event->{
            currentFloor = "L2";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(1));
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().add(pathGroup);
        });
        btn1.setOnMouseClicked(event->{
            currentFloor = "1";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(3));
            locationCanvas.getChildren().add(pathGroup);
        });
        btn2.setOnMouseClicked(event->{
            currentFloor = "2";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(4));
            locationCanvas.getChildren().add(pathGroup);
        });
        btn3.setOnMouseClicked(event->{
            currentFloor = "3";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(5));
            locationCanvas.getChildren().add(pathGroup);
        });
    }

    @FXML
    public void hoverHelp() {
        helpIcon.setOnMouseClicked(
                event -> {
                    final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/PathfindingHelpPopOver.fxml"));
                    PopOver popOver = new PopOver();
                    popOver.setDetachable(true);
                    popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
                    popOver.setArrowSize(0.0);
                    try {
                        popOver.setContentNode(popupLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    popOver.show(helpIcon);
                });
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
