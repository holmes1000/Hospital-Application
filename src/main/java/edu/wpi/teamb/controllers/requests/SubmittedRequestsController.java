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
    //regular class fields
    private String filterCategorySelection;
    private String filterOptionsSelection;

    @FXML
    public void initialize() throws IOException, SQLException {
        initNavBar();
        allRequestsE = new EAllRequests();
        initScrollPane();
        initComboBoxChangeListeners();
        loadRequestsIntoContainer();
        filterCategorySelection = "";
        filterOptionsSelection = "";
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
        if(filterCategory.equals("Status")) {
            if(filterOption.equals("Completed")) {
                filteredListOfRequests = listOfRequests.stream()
                        .filter(request -> request.getRequestStatus().equals("Completed")) // Filter based on the field
                        .collect(Collectors.toCollection(ArrayList::new));
            }
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
        cbFilterCategory.getItems().addAll("", "Status", "Assigned To Me");
        //add change listener to cbFilterCategory
        cbFilterCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterCategorySelection = newValue;
            if (filterCategorySelection.equals("")){
                //if the filterCategorySelection is empty, set cbFilterOptions to invisible
                cbFilterOptions.setVisible(false);
                //set the filterOptionsSelection to empty string
                filterOptionsSelection = "";
                //clear current selection from cbFilterOptions
                cbFilterOptions.getSelectionModel().clearSelection();
                //clear the list of items from cbFilterOptions
                cbFilterOptions.getItems().clear();
            } else {
                //if the filterCategorySelection is not empty, set cbFilterOptions to visible
                cbFilterOptions.setVisible(true);
                //check for whether a filter options selection has already been made
                if ((!filterOptionsSelection.equals(""))) {
                    //clear the filterOptionsSelection
                    filterOptionsSelection = "";
                    //clear current selection from cbFilterOptions
                    cbFilterOptions.getSelectionModel().clearSelection();
                    //clear the list of items from cbFilterOptions
                    cbFilterOptions.getItems().clear();
                }

                switch(cbFilterCategory.getSelectionModel().getSelectedItem()) {
                    case "Status":
                        cbFilterOptions.getItems().clear();
                        cbFilterOptions.getItems().addAll("", "Pending", "Canceled", "Completed");
                        break;
                    case "Assigned To Me":
                        cbFilterOptions.getItems().clear();
                        cbFilterOptions.getItems().addAll("", "Yes", "No");
                        break;
                    default:
                        break;
                }
            }
        });

        //add change listener to cbFilterOptions
        cbFilterOptions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterOptionsSelection = newValue;
            if(!filterOptionsSelection.equals("")) {
                loadRequestsIntoContainer(filterCategorySelection, filterOptionsSelection);
            }
        });
    }
}