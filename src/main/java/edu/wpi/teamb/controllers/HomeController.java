package edu.wpi.teamb.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.EHome;
import edu.wpi.teamb.entities.ELogin;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeController {
    @FXML private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private Pane navPane;
    @FXML private Pane homePane;
    @FXML private MFXButton btnAbout;
    @FXML private MFXButton btnCredits;
    @FXML private  MFXButton btnSecret;
    @FXML private MFXButton btnClear;
    @FXML private VBox userRequestsVBox;
    @FXML private MFXScrollPane userRequestsScrollPane;
    private MFXButton pathfinderImgBtn = new MFXButton();
    Bounds bounds;
    private EHome homeE;

    @FXML
    public void initialize() throws IOException {
        initNavBar();
//        initPathfinderBtn();
        initializeBtns();
        homeE = new EHome();
        bounds = homePane.getBoundsInLocal();
        initScrollPane();
        loadRequestsIntoContainer();
    }

    private void initScrollPane() {
        // Scroll pane preferences
        userRequestsScrollPane.setFitToWidth(true);
        userRequestsScrollPane.pannableProperty().set(true);
        userRequestsScrollPane.hbarPolicyProperty().setValue(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        userRequestsScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private void loadRequestsIntoContainer() {
        //getting the list of all requests
        ArrayList<Request> listOfRequests = homeE.getAllRequests();
        ArrayList<Request> currentUserRequests = new ArrayList<>();
        //filtering the current user's requests
        for(int i = 0; i < listOfRequests.size(); i++) {
            if(listOfRequests.get(i).getEmployee().equals(ELogin.getLogin().getUsername())) {
                currentUserRequests.add(listOfRequests.get(i));
            }
        }
        //traversing the filtered list of requests to add them to the container
        for (int i = 0; i < currentUserRequests.size(); i++) {
            //implementation using FXMLLoader and InfoCardController
            FXMLLoader loader = null;
            //AnchorPane should be the root of the InfoCard.fxml
            AnchorPane requestInfoCardRoot = null;
            //controller for the corresponding InfoCard.fxml
            InfoCardController requestInfoCardController = null;
            try {
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/InfoCard.fxml"));
                requestInfoCardRoot = loader.load();
                requestInfoCardController = loader.getController();
            } catch (IOException e) {
                System.out.println("IOException in loadRequestsIntoContainer of AllRequestsController: " + e.getMessage());
            }

            switch (currentUserRequests.get(i).getRequestType()) {
                case "Meal":
                    IFull fullMealRequest = homeE.getMealRequest(currentUserRequests.get(i).getId());
                    if(fullMealRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullMealRequest);
                    break;
                case "Conference":
                    IFull fullConferenceRequest = homeE.getConferenceRequest(currentUserRequests.get(i).getId());
                    if(fullConferenceRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullConferenceRequest);
                    break;
                case "Flower":
                    IFull fullFlowerRequest = homeE.getFlowerRequest(currentUserRequests.get(i).getId());
                    if(fullFlowerRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFlowerRequest);
                    break;
                case "Office":
                    IFull fullOfficeRequest = homeE.getOfficeRequest(currentUserRequests.get(i).getId());
                    if(fullOfficeRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullOfficeRequest);
                    break;
                case "Furniture":
                    IFull fullFurnitureRequest = homeE.getFurnitureRequest(currentUserRequests.get(i).getId());
                    if(fullFurnitureRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFurnitureRequest);
                    break;
                default:
                    //continue statement to skip any unrecognized types of request to avoid occurrence of empty cards
                    continue;
            }


//           add the request info card to the request container VBox
            //userRequestsVBox X -> allRequestsContainerVBox
            userRequestsVBox.getChildren().add(requestInfoCardRoot);
//            add a margin to the children of allRequestsContainerVBox
            VBox.setMargin(requestInfoCardRoot, new Insets( 0, 0, 10, 0));
        }

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


    Circle circle = new Circle();
    Image img = new Image("edu/wpi/teamb/img/secret.png");
    ImageView secretView = new ImageView(img);

    private void secret(Boolean go){
        btnSecret.setVisible(false);
        secretView.setImage(img);
        secretView.setFitHeight(150);
        secretView.setPreserveRatio(true);
        secretView.setVisible(true);
        secretView.setX(300);
        secretView.setY(300);
        btnClear.setVisible(true);
        if (!homePane.getChildren().contains(secretView)) {homePane.getChildren().add(secretView);}
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {
            double deltaX = 2;
            double deltaY = 2;

            @Override
            public void handle(ActionEvent actionEvent) {

                secretView.setX(secretView.getX() + deltaX);
                secretView.setY(secretView.getY() + deltaY);

//                System.out.println(bounds);
                boolean rightBorder = secretView.getX() >= (bounds.getMaxX());
                boolean leftBorder = secretView.getX() <= (bounds.getMinX());
                boolean bottomBorder = secretView.getY() >= (bounds.getMaxY());
                boolean topBorder = secretView.getY() <= (bounds.getMinY());

                if (rightBorder || leftBorder) {
                    deltaX *= -1;
                }
                if (bottomBorder || topBorder) {
                    deltaY *= -1;
                }
                homePane.toFront();
                secretView.toFront();
            }

        }));
        if (go) {
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
        else {
//            timeline.stop();
//            btnClear.setVisible(false);
//            btnSecret.setVisible(true);
//            homePane.getChildren().remove(secretView);
//            secretView = new ImageView();
            Navigation.navigate(Screen.HOME);
        }

    }

    public void clearSecret(){


    }

    private void initializeBtns() {
        btnCredits.setOnMouseClicked(e -> handleCredits());
        btnAbout.setOnMouseClicked(e -> handleAbout());
        btnSecret.setOnMouseClicked(e -> secret(true));
        btnClear.setOnMouseClicked(e -> secret(false));
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
