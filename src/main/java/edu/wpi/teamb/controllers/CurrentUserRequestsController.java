package edu.wpi.teamb.controllers;

import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.EHome;
import edu.wpi.teamb.entities.ELogin;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class CurrentUserRequestsController {
    @FXML private VBox userRequestsVBox;
    @FXML private MFXScrollPane userRequestsScrollPane;
    private EHome homeE;

    @FXML
    public void initialize() throws IOException {
        homeE = new EHome();
        initScrollPane();
        loadRequestsIntoContainer();
    }

    private void loadRequestsIntoContainer() {
        //getting the list of all requests
        ArrayList<Request> listOfRequests = homeE.getAllRequests();
        ArrayList<Request> currentUserRequests = new ArrayList<>();
        //filtering the current user's requests
        for (int i = 0; i < listOfRequests.size(); i++) {
            if (listOfRequests.get(i).getEmployee().equals(ELogin.getLogin().getUsername()) && !listOfRequests.get(i).getRequestStatus().equals("Completed")) {
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
                case "Translation":
                    IFull fullTranslationRequest = homeE.getTranslationRequest(currentUserRequests.get(i).getId());
                    if(fullTranslationRequest == null){continue;}
                    Objects.requireNonNull(requestInfoCardController).sendRequest(fullTranslationRequest);
                default:
                    //continue statement to skip any unrecognized types of request to avoid occurrence of empty cards
                    continue;
            }
            requestInfoCardController.enterViewCurrentUserRequestsMode();

//           add the request info card to the request container VBox
            //userRequestsVBox X -> allRequestsContainerVBox
            userRequestsVBox.getChildren().add(requestInfoCardRoot);
//            add a margin to the children of allRequestsContainerVBox
            VBox.setMargin(requestInfoCardRoot, new Insets( 0, 0, 10, 0));
        }
    }

    private void initScrollPane() {
        // Scroll pane preferences
        userRequestsScrollPane.setFitToWidth(true);
        userRequestsScrollPane.pannableProperty().set(true);
        userRequestsScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        userRequestsScrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);
    }
}
