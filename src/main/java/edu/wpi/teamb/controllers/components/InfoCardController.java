package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.controllers.requests.ConferenceRequestControllerI;
import edu.wpi.teamb.controllers.requests.FlowerRequestControllerI;
import edu.wpi.teamb.controllers.requests.IRequestController;
import edu.wpi.teamb.controllers.requests.MealRequestControllerI;
import edu.wpi.teamb.entities.components.EInfoCard;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class InfoCardController {
  @FXML private AnchorPane requestInfoAnchorPane;
  @FXML private ImageView requestTypeIconImageView;
  @FXML private MFXButton editButton;
  @FXML private MFXButton deleteButton;
  @FXML private MFXButton submitButton;

  @FXML private Label requestIdLabel;
  @FXML private Label dateSubmittedLabel;
  @FXML private Label timeSubmittedLabel;
  @FXML private Label locationNameLabel;
  @FXML private Label employeeAssignedLabel;
  @FXML private Label statusLabel; //deprecated implementation comment: make a button for this *Note: Please Disregard*
  @FXML private VBox buttonContainerVBox;
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
    editButton.setOnMouseClicked(event -> {
        FXMLLoader loader = null;
        Scene editPageScene = null;
        IRequestController controller = null;
        //create a new window to load the edit page
        Stage editPageStage = new Stage();
        //switch statement to load the correct edit page
        switch (requestType){
            case "Meal":
                //load the meal request form
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/MealRequest.fxml"));
                break;
            case "Conference":
                //load the conference request form
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/ConferenceRequest.fxml"));
                break;
            case "Flower":
                //load the flower request form
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/FlowerRequests.fxml"));
                break;
            case "Office":
                //load the office request form
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/OfficeRequest.fxml"));
                break;
            case "Furniture":
                //load the furniture request form
                loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/requests/FurnitureRequest.fxml"));
                break;
            default:
                break;
        }
        //show the edit page window
        try{
            editPageScene = new Scene(loader.load());
            controller = loader.getController();
        } catch (IOException e) {
            System.out.println("Error loading edit conference request form" + e.getMessage());
        }
        //determine and send current information to the edit page
        switch(requestType){
            case "Meal":
                //send the current meal request information to the edit page
                ((MealRequestControllerI) controller).enterMealRequestEditableMode((FullMealRequest) fullRequest, this);
                break;
            case "Conference":
                //send the current conference request information to the edit page
                ((ConferenceRequestControllerI) controller).enterConferenceRequestEditableMode((FullConferenceRequest) fullRequest, this);
                break;
            case "Flower":
                //send the current flower request information to the edit page
                ((FlowerRequestControllerI) controller).enterFlowerRequestEditableMode((FullFlowerRequest) fullRequest, this);
                break;
//            case "Office":
//                //send the current office request information to the edit page
//                ((OfficeRequestControllerI) controller).sendRequest(fullRequest);
//                break;
//            case "Furniture":
//                //send the current furniture request information to the edit page
//                ((FurnitureRequestControllerI) controller).sendRequest(fullRequest);
//                break;
            default:
                break;
        }
        editPageStage.setScene(editPageScene);
        editPageStage.show();
    });
  }

  public void sendRequest(IFull request) {
    this.fullRequest = request;
    requestType = fullRequest.getRequestType();
    getSpecialFields(requestType);
    //setting common components with common request info
    setRequestIdLabel(Integer.toString(fullRequest.getId()));
    if (fullRequest.getDateSubmitted() != null) {
        //break up the date and time into two different variables
        String[] dateAndTime = fullRequest.getDateSubmitted().toString().split(" ");
        setDateSubmittedLabel(dateAndTime[0]);
        setTimeSubmittedLabel(dateAndTime[1]);
    } else {
        setDateSubmittedLabel("Unavailable");
        setTimeSubmittedLabel("Unavailable");
    }
    setLocationNameLabel(fullRequest.getLocationName());
    setEmployeeAssignedLabel(fullRequest.getEmployee());
    setStatusLabel(fullRequest.getRequestStatus());
    requestTypeIconImageView.setImage(fullRequest.setRequestTypeIconImageView());
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
            ((FullMealRequest) fullRequest).setNotes(mealFull.getNotes());
        } else if (Objects.equals(requestType, "Conference")) {
            FullConferenceRequest conferenceFull = (FullConferenceRequest) fullRequest;
            //set the conference specific fields
            ((FullConferenceRequest) fullRequest).setBookingReason(conferenceFull.getBookingReason());
            ((FullConferenceRequest) fullRequest).setEventName(conferenceFull.getEventName());
            ((FullConferenceRequest) fullRequest).setDuration(conferenceFull.getDuration());
            ((FullConferenceRequest) fullRequest).setDateRequested(conferenceFull.getDateRequested());
            (fullRequest).setNotes(conferenceFull.getNotes());
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
        setSpecificFieldsOnCard();
    }

  public void setSpecificFieldsOnCard() {
      //empty subComponentContainer *necessary for update*
      subComponentContainer.getChildren().clear();
      //set a margin on subComponentContainer
      subComponentContainer.setSpacing(5);
      String commonCSStyles = "-fx-font-size: 14px; -fx-text-fill: WHITE;";
      if (Objects.equals(requestType, "Meal")) {
          //make the labels and set the styling
          Label orderFromLabel = new Label("Order From: " + ((FullMealRequest) fullRequest).getOrderFrom());
          orderFromLabel.setStyle(commonCSStyles);
          Label foodLabel = new Label("Food: " + ((FullMealRequest) fullRequest).getFood());
          foodLabel.setStyle(commonCSStyles);
          Label drinkLabel = new Label("Drink: " + ((FullMealRequest) fullRequest).getDrink());
          drinkLabel.setStyle(commonCSStyles);
          Label snackLabel = new Label("Snack: " + ((FullMealRequest) fullRequest).getSnack());
          snackLabel.setStyle(commonCSStyles);
          //set the meal specific fields
          subComponentContainer.getChildren().add(orderFromLabel);
          subComponentContainer.getChildren().add(foodLabel);
          subComponentContainer.getChildren().add(drinkLabel);
          subComponentContainer.getChildren().add(snackLabel);
      } else if (Objects.equals(requestType, "Conference")) {
          //make the labels and set the styling
          Label eventNameLabel = new Label("Event Name: " + ((FullConferenceRequest) fullRequest).getEventName());
          eventNameLabel.setStyle(commonCSStyles);
          Label bookingReasonLabel = new Label("Booking Reason: " + ((FullConferenceRequest) fullRequest).getBookingReason());
          bookingReasonLabel.setStyle(commonCSStyles);
          Label durationLabel = new Label("Duration: " + ((FullConferenceRequest) fullRequest).getDuration());
          durationLabel.setStyle(commonCSStyles);
          Label dateRequestedLabel = new Label("Date Requested: " + ((FullConferenceRequest) fullRequest).getDateRequested());
          dateRequestedLabel.setStyle(commonCSStyles);
          //set the conference specific fields
          subComponentContainer.getChildren().add(eventNameLabel);
          subComponentContainer.getChildren().add(bookingReasonLabel);
          subComponentContainer.getChildren().add(durationLabel);
          subComponentContainer.getChildren().add(dateRequestedLabel);
      } else if (Objects.equals(requestType, "Flower")) {
          //make the labels and set the styling
          Label flowerTypeLabel = new Label("Flower Type: " + ((FullFlowerRequest) fullRequest).getFlowerType());
          flowerTypeLabel.setStyle(commonCSStyles);
          Label sizeLabel = new Label("Size: " + ((FullFlowerRequest) fullRequest).getSize());
          sizeLabel.setStyle(commonCSStyles);
          Label colorLabel = new Label("Color: " + ((FullFlowerRequest) fullRequest).getColor());
          colorLabel.setStyle(commonCSStyles);
          Label messageLabel = new Label("Message: " + ((FullFlowerRequest) fullRequest).getMessage());
          messageLabel.setStyle(commonCSStyles);
          //set the flower specific fields
          subComponentContainer.getChildren().add(flowerTypeLabel);
          subComponentContainer.getChildren().add(sizeLabel);
          subComponentContainer.getChildren().add(colorLabel);
          subComponentContainer.getChildren().add(messageLabel);
      } else if (Objects.equals(requestType, "Furniture")) {
          //make the labels and set the styling
          Label typeLabel = new Label("Type: " + ((FullFurnitureRequest) fullRequest).getType());
          typeLabel.setStyle(commonCSStyles);
          Label modelLabel = new Label("Model: " + ((FullFurnitureRequest) fullRequest).getModel());
          modelLabel.setStyle(commonCSStyles);
          Label assemblyLabel = new Label("Assembly: " + ((FullFurnitureRequest) fullRequest).getAssembly());
          assemblyLabel.setStyle(commonCSStyles);
          //set the furniture specific fields
          subComponentContainer.getChildren().add(typeLabel);
          subComponentContainer.getChildren().add(modelLabel);
          subComponentContainer.getChildren().add(assemblyLabel);
      } else if (Objects.equals(requestType, "Office")) {
          //make the labels and set the styling
          Label itemLabel = new Label("Item: " + ((FullOfficeRequest) fullRequest).getItem());
          itemLabel.setStyle(commonCSStyles);
          Label quantityLabel = new Label("Quantity: " + ((FullOfficeRequest) fullRequest).getQuantity());
          quantityLabel.setStyle(commonCSStyles);
          Label typeLabel = new Label("Type: " + ((FullOfficeRequest) fullRequest).getType());
          typeLabel.setStyle(commonCSStyles);
          //set the office specific fields
          subComponentContainer.getChildren().add(itemLabel);
          subComponentContainer.getChildren().add(quantityLabel);
          subComponentContainer.getChildren().add(typeLabel);
      }
  }

  /**
   * Sets the text of the requestId TextField to the requestId parameter
   *
   * @param requestId
   */
  private void setRequestIdLabel(String requestId) {
    requestIdLabel.setText(requestId);
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
   * Sets the text of the timeSubmitted TextField to the timeSubmitted parameter
   *
   * @param timeSubmitted
   */
  public void setTimeSubmittedLabel(String timeSubmitted) {
    timeSubmittedLabel.setText(timeSubmitted);
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
