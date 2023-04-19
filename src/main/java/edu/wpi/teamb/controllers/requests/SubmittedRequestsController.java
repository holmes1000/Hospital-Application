package edu.wpi.teamb.controllers.requests;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.AllRequestsE;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class SubmittedRequestsController {
    @FXML private MFXButton btnCancel;
    @FXML private ImageView helpIcon;
    @FXML private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private VBox allRequestsContainerVBox;
    @FXML private ScrollPane allRequestsScrollPane;
    //entity object of class that contains all the methods to get the requests
    private AllRequestsE allRequestsE;

    @FXML
    public void initialize() throws IOException, SQLException {
        initNavBar();
        allRequestsE = new AllRequestsE();
        initScrollPane();
        loadRequestsIntoContainer();
    }

    private void initScrollPane() {
        // Scroll pane preferences
        allRequestsScrollPane.setFitToWidth(true);
        allRequestsScrollPane.pannableProperty().set(true);
        allRequestsScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        allRequestsScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private void loadRequestsIntoContainer() {
        //getting the list of all requests
        ArrayList<Request> listOfRequests = allRequestsE.getAllRequests();
        for (int i = 0; i < listOfRequests.size(); i++) {
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


            switch (listOfRequests.get(i).getRequestType()) {
                case "Meal":
                    FullMealRequest fullMealRequest = allRequestsE.getMealRequest(listOfRequests.get(i).getId());
                    requestInfoCardController.sendRequest(fullMealRequest);
                    break;
                case "Conference":
                    FullConferenceRequest fullConferenceRequest = allRequestsE.getConfenferenceRequest(listOfRequests.get(i).getId());
                    requestInfoCardController.sendRequest(fullConferenceRequest);
                    break;
                case "Flower":
                    FullFlowerRequest fullFlowerRequest = allRequestsE.getFlowerRequest(listOfRequests.get(i).getId());
                    requestInfoCardController.sendRequest(fullFlowerRequest);
                    break;
                case "Office":
                    FullOfficeRequest fullOfficeRequest = allRequestsE.getOfficeRequest(listOfRequests.get(i).getId());
                    requestInfoCardController.sendRequest(fullOfficeRequest);
                    break;
//                case "Furniture":
//                    FullFurnitureRequest fullFurnitureRequest = allRequests.getFurnitureRequest(listOfRequests.get(i).getId());
//                    break;
                default:
                    break;
            }


//           add the request info card to the request container VBox
            allRequestsContainerVBox.getChildren().add(requestInfoCardRoot);
//            add a margin to the children of allRequestsContainerVBox
            VBox.setMargin(requestInfoCardRoot, new Insets(10, 0, 10, 0));
        }
    }

    @FXML
    private FXMLLoader getInfoCard() {
        try {
            return FXMLLoader.load(getClass().getResource("/edu/wpi/teamb/views/components/InfoCard.fxml"));
        } catch (IOException e) {
            System.out.println("IOException in getInfoCard() of AllRequestsController: " + e.getMessage());
        }
        return null;
    }

    @FXML
    public void clickCancel() {
        btnCancel.setOnMouseClicked(event -> {
            Navigation.navigate(Screen.CREATE_NEW_REQUEST);
        });
    }

    @FXML
    public void hoverHelp() {
        helpIcon.setOnMouseEntered(
                event -> {
                    Tooltip helpTip =
                            new Tooltip(
                                    "On this page: Please select what request to look at.\n"
                                            + "The selected requests information is listed on the right.\n"
                                            + "If possible, you can decide to edit the request. \n");
                    helpTip.setStyle("-fx-size: 18");
                    helpTip.setShowDelay(Duration.millis(1));
                    helpTip.hideDelayProperty().set(Duration.seconds(1.5));
                    Tooltip.install(helpIcon, helpTip);
                });
        helpIcon.setOnMouseExited(event -> {});
    }

    private void initializeFields() {
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

    @FXML
    public void clickExit() {
        System.exit(0);
    }
}