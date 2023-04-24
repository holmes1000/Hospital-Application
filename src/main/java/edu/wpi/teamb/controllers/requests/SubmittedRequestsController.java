package edu.wpi.teamb.controllers.requests;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EAllRequests;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.palexdev.materialfx.controls.MFXComboBox;
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
    @FXML private MFXComboBox<String> cbFilterCategory;
    @FXML private MFXComboBox<String> cbFilterOptions;

    //entity object of class that contains all the methods to get the requests
    private EAllRequests allRequestsE;
    //regular class fields below


    @FXML
    public void initialize() throws IOException, SQLException {
        initNavBar();
        allRequestsE = new EAllRequests();
        initScrollPane();
        initComboBoxChangeListeners();
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
                    IFull fullMealRequest = allRequestsE.getMealRequest(listOfRequests.get(i).getId());
                    if(fullMealRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullMealRequest);
                    break;
                case "Conference":
                    IFull fullConferenceRequest = allRequestsE.getConferenceRequest(listOfRequests.get(i).getId());
                    if(fullConferenceRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullConferenceRequest);
                    break;
                case "Flower":
                    IFull fullFlowerRequest = allRequestsE.getFlowerRequest(listOfRequests.get(i).getId());
                    if(fullFlowerRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFlowerRequest);
                    break;
                case "Office":
                    IFull fullOfficeRequest = allRequestsE.getOfficeRequest(listOfRequests.get(i).getId());
                    if(fullOfficeRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullOfficeRequest);
                    break;
                case "Furniture":
                    IFull fullFurnitureRequest = allRequestsE.getFurnitureRequest(listOfRequests.get(i).getId());
                    if(fullFurnitureRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFurnitureRequest);
                    break;
                default:
                    //continue statement to skip any unrecognized types of request to avoid occurrence of empty cards
                    continue;
            }


//           add the request info card to the request container VBox
            allRequestsContainerVBox.getChildren().add(requestInfoCardRoot);
//            add a margin to the children of allRequestsContainerVBox
            VBox.setMargin(requestInfoCardRoot, new Insets( 0, 0, 10, 0));
        }
    }

    private void loadRequestsIntoContainer(String filterCategory, String filterOption) {
        //clearing the container VBox
        allRequestsContainerVBox.getChildren().clear();
        //getting the list of all requests
        ArrayList<Request> listOfRequests = allRequestsE.getAllRequests();
        //applying status filter settings
        ArrayList<Request> filteredListOfRequests = new ArrayList<>();
        if (filterCategory.equals("Status")) {
            filteredListOfRequests = listOfRequests.stream()
                    .filter(request -> request.getRequestStatus().equals(filterOption))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else if (filterCategory.equals("Request Type")) {
            filteredListOfRequests = listOfRequests.stream()
                    .filter(request -> request.getRequestType().equals(filterOption))
                    .collect(Collectors.toCollection(ArrayList::new));
        }


        for (int i = 0; i < filteredListOfRequests.size(); i++) {
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

            switch (filteredListOfRequests.get(i).getRequestType()) {
                case "Meal":
                    IFull fullMealRequest = allRequestsE.getMealRequest(filteredListOfRequests.get(i).getId());
                    if(fullMealRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullMealRequest);
                    break;
                case "Conference":
                    IFull fullConferenceRequest = allRequestsE.getConferenceRequest(filteredListOfRequests.get(i).getId());
                    if(fullConferenceRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullConferenceRequest);
                    break;
                case "Flower":
                    IFull fullFlowerRequest = allRequestsE.getFlowerRequest(filteredListOfRequests.get(i).getId());
                    if(fullFlowerRequest == null) {continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFlowerRequest);
                    break;
                case "Office":
                    IFull fullOfficeRequest = allRequestsE.getOfficeRequest(filteredListOfRequests.get(i).getId());
                    if(fullOfficeRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullOfficeRequest);
                    break;
                case "Furniture":
                    IFull fullFurnitureRequest = allRequestsE.getFurnitureRequest(filteredListOfRequests.get(i).getId());
                    if(fullFurnitureRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullFurnitureRequest);
                    break;
                default:
                    //continue statement to skip any unrecognized types of request to avoid occurrence of empty cards
                    continue;
            }


//           add the request info card to the request container VBox
            allRequestsContainerVBox.getChildren().add(requestInfoCardRoot);
//            add a margin to the children of allRequestsContainerVBox
            VBox.setMargin(requestInfoCardRoot, new Insets( 0, 0, 10, 0));
        }
    }

    @FXML
    public void clickCancel() {
        Navigation.navigate(Screen.CREATE_NEW_REQUEST);
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

    /**
     * This method will add changeListeners to the combo boxes
     * in order to update some state variables.
     */
    private void initComboBoxChangeListeners() {
        //at the beginning set cbFilterOptions to invisible
        cbFilterOptions.setVisible(false);
        //add filtering options to cbFilterCategory
        cbFilterCategory.getItems().addAll("", "Status", "Request Type");
        //add change listener to cbFilterCategory
        cbFilterCategory.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        //if selection is null
                        loadRequestsIntoContainer();
                    } else if (!newValue.equals("")) {
                        //set cbFilterOptions to visible
                        cbFilterOptions.setVisible(true);
                        //clear the items in cbFilterOptions
                        cbFilterOptions.getItems().clear();
                        //add filtering options to cbFilterOptions based on filter category
                        switch (newValue) {
                            case "Status":
                                cbFilterOptions.getItems().addAll("", RequestStatus.PENDING.getStatus(), RequestStatus.COMPLETED.getStatus());
                                break;
                            case "Request Type":
                                cbFilterOptions.getItems().addAll(
                                        "",
                                        RequestType.MEAL.getType(),
                                        RequestType.CONFERENCE.getType(),
                                        RequestType.FLOWER.getType(),
                                        RequestType.OFFICE.getType(),
                                        RequestType.FURNITURE.getType());
                                break;
                        }
                    } else {
                        //if selection is empty
                        //set cbFilterOptions to invisible
                        cbFilterOptions.setVisible(false);
                        //clear the items in cbFilterOptions
                        cbFilterOptions.getItems().clear();
                        //clear cBFilterOptions selection
                        cbFilterOptions.getSelectionModel().clearSelection();
                        //reset all requests in the container
                        loadRequestsIntoContainer();
                    }
                });

        //add change listener to cbFilterOptions
        cbFilterOptions.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) {
                        //if selection is null
                        loadRequestsIntoContainer();
                    } else if (!newValue.equals("")) {
                        //if the new value is not empty
                        //set filterOption to the new value
                        loadRequestsIntoContainer(cbFilterCategory.getSelectedItem(), newValue);
                    } else {
                        //set filterOption to the new value
                        loadRequestsIntoContainer();
                    }
                });
    }
}

enum RequestStatus {
    PENDING("Pending"),
    COMPLETED("Completed");
    private final String status;

    RequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

enum RequestType {
    MEAL("Meal"),
    CONFERENCE("Conference"),
    FLOWER("Flower"),
    OFFICE("Office"),
    FURNITURE("Furniture");
    private final String type;

    RequestType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}