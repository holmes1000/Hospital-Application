package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.entities.components.EInfoCard;
import edu.wpi.teamb.entities.requests.IRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class InfoCardController {
  @FXML private AnchorPane requestInfoAnchorPane;
  @FXML private ImageView requestTypeIconImageView;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private Label dateSubmittedLabel;
  @FXML private Label locationNameLabel;
  @FXML private Label employeeAssignedLabel;
  @FXML private Label statusLabel; //make a button for this
  @FXML private VBox buttonContainerVBox;
  @FXML private MFXButton submitButton;
  @FXML private Label roomNumberLabel;
  @FXML private VBox subComponentContainer;

  //fields for different conference request types
  FullFactory fullFactory = new FullFactory();
  IFull fullRequest;
  private String requestType;

  //object of the entity class that contains all the methods to get the requests
  private EInfoCard EInfoCard;


  @FXML public void initialize() throws IOException {
    EInfoCard = new EInfoCard();
    //set margin between buttons in VBox
    buttonContainerVBox.setSpacing(10);
    initBtns();
  }

  private void initBtns() {
    deleteButton.setOnMouseClicked(
        event -> {
          //remove the request from the list of requests
          requestInfoAnchorPane.setVisible(false);
          ((VBox) requestInfoAnchorPane.getParent()).getChildren().remove(requestInfoAnchorPane);
        });
    submitButton.setOnAction(
            event -> {
              //set the submit button to hidden
              submitButton.setVisible(false);
            });
    editButton.setOnMouseClicked(event -> {});
  }

  public void sendRequest(IFull request) {
    this.fullRequest = request;
    requestType = fullRequest.getRequestType();
    getSpecialFields(requestType);
    //setting common components with common request info
    if (fullRequest.getDateSubmitted() != null) { setDateSubmittedLabel(fullRequest.getDateSubmitted().toString()); }
    setLocationNameLabel(fullRequest.getLocationName());
    setEmployeeAssignedLabel(fullRequest.getEmployee());
    setStatusLabel(fullRequest.getRequestStatus());
    fullRequest.setRequestType();
  }

  void getSpecialFields(String requestType) {
    if (Objects.equals(requestType, "Meal")) {
        FullMealRequest mealFull = (FullMealRequest) fullRequest;
        //set the meal specific fields
        ((FullMealRequest) fullRequest).setDrink(mealFull.getDrink());
        ((FullMealRequest) fullRequest).setFood(mealFull.getFood());
        ((FullMealRequest) fullRequest).setOrderFrom(mealFull.getOrderFrom());
        ((FullMealRequest) fullRequest).setSnack(mealFull.getSnack());

        } else if (Objects.equals(requestType, "Conference")) {
        FullConferenceRequest conferenceFull = (FullConferenceRequest) fullRequest;
        //set the conference specific fields
        ((FullConferenceRequest) fullRequest).setBookingReason(conferenceFull.getBookingReason());
        ((FullConferenceRequest) fullRequest).setEventName(conferenceFull.getEventName());
        ((FullConferenceRequest) fullRequest).setDuration(conferenceFull.getDuration());
        ((FullConferenceRequest) fullRequest).setDateRequested(conferenceFull.getDateRequested());

        } else if (Objects.equals(requestType, "Flower")) {
        FullFlowerRequest flowerFull = (FullFlowerRequest) fullRequest;
        //set the flower specific fields
        ((FullFlowerRequest) fullRequest).setFlowerType(flowerFull.getFlowerType());
        ((FullFlowerRequest) fullRequest).setSize(flowerFull.getSize());
        ((FullFlowerRequest) fullRequest).setColor(flowerFull.getColor());
        ((FullFlowerRequest) fullRequest).setMessage(flowerFull.getMessage());

        } else if (Objects.equals(requestType, "Furniture")) {
        FullFurnitureRequest furnitureRequest = (FullFurnitureRequest) fullRequest;
        //set the furniture specific fields
        ((FullFurnitureRequest) fullRequest).setType(furnitureRequest.getType());
        ((FullFurnitureRequest) fullRequest).setModel(furnitureRequest.getModel());
        ((FullFurnitureRequest) fullRequest).setAssembly(furnitureRequest.getAssembly());

        } else if (Objects.equals(requestType, "Office")) {
        FullOfficeRequest officeRequest = (FullOfficeRequest) fullRequest;
        //set the office specific fields
        ((FullOfficeRequest) fullRequest).setQuantity(officeRequest.getQuantity());
        ((FullOfficeRequest) fullRequest).setItem(officeRequest.getItem());
        ((FullOfficeRequest) fullRequest).setType(officeRequest.getType());

    }
  }

  /**
   * Sets the text of the dateSubmitted TextField to the dateSubmitted parameter
   *
   * @param dateSubmitted
   */
  public void setDateSubmittedLabel(String dateSubmitted) {
    dateSubmittedLabel.setText(dateSubmitted);
  }

  /**
   * Sets the text of the locationName TextField to the locationName parameter
   *
   * @param locationName
   */
  public void setLocationNameLabel(String locationName) {
    locationNameLabel.setText(locationName);
  }

  /**
   * Sets the text of the employeeAssigned TextField to the employeeAssigned parameter
   *
   * @param employeeAssigned
   */
  public void setEmployeeAssignedLabel(String employeeAssigned) {
    employeeAssignedLabel.setText(employeeAssigned);
  }

  /**
   * Sets the text of the status TextField to the status parameter
   *
   * @param status
   */
  public void setStatusLabel(String status) {
    statusLabel.setText(status);
  }



}
