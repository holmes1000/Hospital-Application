package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.FullFlowerRequest;
import edu.wpi.teamb.DBAccess.FullMealRequest;
import edu.wpi.teamb.DBAccess.FullOfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.entities.components.EInfoCard;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

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
  private String requestType;
  private FullConferenceRequest fullConferenceRequest;
  private FullMealRequest fullMealRequest;
  private FullFlowerRequest fullFlowerRequest;
  private FullOfficeRequest fullOfficeRequest;

  //edit FXML components that are programmatically added
  private MFXComboBox<String> employeeAssignedComboBox;
  private MFXComboBox<String> locationNameComboBox;
  private MFXComboBox<String> statusComboBox;

  //fields for subcomponents begin here
  private AnchorPane subComponentRoot;
  IInfoCardSubComponentController subComponentController;
  //object of the entity class that contains all the methods to get the requests
  private EInfoCard EInfoCard;
  boolean editable = false;


  @FXML public void initialize() throws IOException {
    EInfoCard = new EInfoCard();
    editable = false;
    //set margin between buttons in VBox
    buttonContainerVBox.setSpacing(10);
    initializeEditButtonOnClickListener();
    initializeSubmitButtonOnClickListener();
    initializeDeleteButtonOnClickListener();
  }

  private void initializeDeleteButtonOnClickListener() {
    deleteButton.setOnMouseClicked(
        event -> {
          //remove the request from the list of requests
          requestInfoAnchorPane.setVisible(false);
          ((VBox) requestInfoAnchorPane.getParent()).getChildren().remove(requestInfoAnchorPane);
        });
  }

  private void initializeSubmitButtonOnClickListener() {
    submitButton.setOnAction(
        event -> {
          locationNameLabel.setText(locationNameComboBox.getValue());
          employeeAssignedLabel.setText(employeeAssignedComboBox.getValue());
          statusLabel.setText(statusComboBox.getValue());
          //remove the combo boxes
          requestInfoAnchorPane.getChildren().remove(locationNameComboBox);
          requestInfoAnchorPane.getChildren().remove(employeeAssignedComboBox);
          requestInfoAnchorPane.getChildren().remove(statusComboBox);
          //set the submit button to hidden
          submitButton.setVisible(false);
        });
  }

  private void editLocationName() {
    if(editable) {
      //get the coordinates of the location label
      double labelXCoord = locationNameLabel.getLayoutX();
      double labelYCoord = locationNameLabel.getLayoutY();

      //create a combo box if null
      if(locationNameComboBox == null) {
        locationNameComboBox = new MFXComboBox<>();
      }
      //set the location of the combo box
      int comboBoxHeightOffset = -10;
      locationNameComboBox.setLayoutX(labelXCoord);
      locationNameComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

      //get the dimensions of the label
      double labelWidth = locationNameLabel.getWidth();
      double labelHeight = locationNameLabel.getHeight();

      //assign the dimensions to the combo box
      locationNameComboBox.setPrefWidth(labelWidth*2);
      locationNameComboBox.setPrefHeight(labelHeight*2);

      //replacement of the label with the combo box
      int index =  requestInfoAnchorPane.getChildren().indexOf(locationNameLabel);
      requestInfoAnchorPane.getChildren().add(index, locationNameComboBox);

      //get the list of location names
      ArrayList<LocationName> locationNamesList = EInfoCard.getLocationNames();
      ArrayList<String> locationNameStrings = new ArrayList<>();
      for (LocationName locationName: locationNamesList) {
        locationNameStrings.add(locationName.getLongName());
      }
      locationNameComboBox.getItems().addAll(locationNameStrings);

      //set the combo box text to the current location name
      locationNameComboBox.setText(locationNameLabel.getText());
    } else {
      requestInfoAnchorPane.getChildren().remove(locationNameComboBox);
      locationNameComboBox = null;
    }
  }

  private void editEmployeeAssigned() {
    if(editable) {
      //get the coordinates of the employee assigned label
      double labelXCoord = employeeAssignedLabel.getLayoutX();
      double labelYCoord = employeeAssignedLabel.getLayoutY();

      //create a combo box if null
      if(employeeAssignedComboBox == null) {
        employeeAssignedComboBox = new MFXComboBox<>();
      }
      //set the location of the combo box
      int comboBoxHeightOffset = -10;
      employeeAssignedComboBox.setLayoutX(labelXCoord);
      employeeAssignedComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

      //get the dimensions of the label
      double labelWidth = employeeAssignedLabel.getWidth();
      double labelHeight = employeeAssignedLabel.getHeight();

      //assign the dimensions to the combo box
      employeeAssignedComboBox.setPrefWidth(labelWidth);
      employeeAssignedComboBox.setPrefHeight(labelHeight);

      //replacement of the label with the combo box
      int index =  requestInfoAnchorPane.getChildren().indexOf(employeeAssignedLabel);
      requestInfoAnchorPane.getChildren().add(index, employeeAssignedComboBox);

      //get the list of employees
      ArrayList<User> userList = EInfoCard.getUsernames();
      ArrayList<String> usernamesList = new ArrayList<>();
      for (User user: userList) {
        usernamesList.add(user.getUsername());
      }
      //add it to the comboBox
      employeeAssignedComboBox.getItems().addAll(usernamesList);

      //set the combo box text to the current employee assigned
      employeeAssignedComboBox.setText(employeeAssignedLabel.getText());
    } else {
      requestInfoAnchorPane.getChildren().remove(employeeAssignedComboBox);
      employeeAssignedComboBox = null;
    }
  }

  private void editStatus() {
    if(editable){
      //get the coordinates of the status label
      double labelXCoord = statusLabel.getLayoutX();
      double labelYCoord = statusLabel.getLayoutY();

      //create a combo box if null
      if(statusComboBox == null) {
        statusComboBox = new MFXComboBox<>();
      }
      //set the location of the combo box
      int comboBoxHeightOffset = -10;
      statusComboBox.setLayoutX(labelXCoord);
      statusComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

      //get the dimensions of the label
      double labelWidth = statusLabel.getWidth();
      double labelHeight = statusLabel.getHeight();

      //assign the dimensions to the combo box
      statusComboBox.setPrefWidth(labelWidth);
      statusComboBox.setPrefHeight(labelHeight);

      //replacement of the label with the combo box
      int index =  requestInfoAnchorPane.getChildren().indexOf(statusLabel);
      requestInfoAnchorPane.getChildren().add(index, statusComboBox);

      //get the list of statuses
      ArrayList<String> statusList = new ArrayList<>();
      statusList.add("Pending");
      statusList.add("In-Progress");
      statusList.add("Finished");
      //add it to the comboBox
      statusComboBox.getItems().addAll(statusList);

      //set the combo box text to the current employee assigned
      statusComboBox.setText(statusLabel.getText());
    } else {
      requestInfoAnchorPane.getChildren().remove(statusComboBox);
      statusComboBox = null;
    }

  }

  /**
   * sends a full meal request to the InfoCardController to be displayed on the InfoCard
   */
  public void sendRequest(FullMealRequest fullMealRequest) {
    //common fields
    this.fullMealRequest = fullMealRequest;
    requestType = fullMealRequest.getRequestType();

    //setting common components with common request info
    setDateSubmittedLabel(fullMealRequest.getDateSubmitted().toString());
    setLocationNameLabel(fullMealRequest.getLocationName());
    setEmployeeAssignedLabel(fullMealRequest.getEmployee());
    setStatusLabel(fullMealRequest.getRequestStatus());
    setRequestType(fullMealRequest.getRequestType());

    //setting specific components that display specific request info
    FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/edu/wpi/teamb/views/components/MealRequestInfoCardSubComponent.fxml"));
    try{
      subComponentRoot = loader.load();
      subComponentController = loader.getController();
    } catch (IOException e){
      System.out.println(e.getMessage());
    }
    //sending it to subComponent
    subComponentController.sendRequest(fullMealRequest);
    //adding the subcomponent to the container
    subComponentContainer.getChildren().add(subComponentRoot);
  }

  /**
   * sends a full conference request to the InfoCardController to be displayed on the InfoCard
   */
  public void sendRequest(FullConferenceRequest fullConferenceRequest) {
    //common fields
    this.fullConferenceRequest = fullConferenceRequest;
    requestType = this.fullConferenceRequest.getRequestType();

    //setting common components with common request info
    setDateSubmittedLabel(fullConferenceRequest.getDateSubmitted().toString());
    setLocationNameLabel(fullConferenceRequest.getLocationName());
    setEmployeeAssignedLabel(fullConferenceRequest.getEmployee());
    setStatusLabel(fullConferenceRequest.getRequestStatus());
    setRequestType(fullConferenceRequest.getRequestType());

    //setting specific components that display specific request info
    FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/edu/wpi/teamb/views/components/ConferenceInfoCardSubComponent.fxml"));
    try{
      subComponentRoot = loader.load();
      subComponentController = loader.getController();
    } catch (IOException e){
      System.out.println(e.getMessage());
    }
    //sending it to subComponent
    subComponentController.sendRequest(fullConferenceRequest);
    //adding the subcomponent to the container
    subComponentContainer.getChildren().add(subComponentRoot);
  }

  /**
   * sends a full flower request to the InfoCardController to be displayed on the InfoCard
   */
  public void sendRequest(FullFlowerRequest fullFlowerRequest) {
    //common fields
    this.fullFlowerRequest = fullFlowerRequest;
    requestType = this.fullFlowerRequest.getRequestType();

    //setting common components with common request info
    setDateSubmittedLabel(fullFlowerRequest.getDateSubmitted().toString());
    setLocationNameLabel(fullFlowerRequest.getLocationName());
    setEmployeeAssignedLabel(fullFlowerRequest.getEmployee());
    setStatusLabel(fullFlowerRequest.getRequestStatus());
    setRequestType(fullFlowerRequest.getRequestType());

    //setting specific components that display specific request info
    FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/edu/wpi/teamb/views/components/FlowerInfoCardSubComponent.fxml"));
    try {
      subComponentRoot = loader.load();
      subComponentController = loader.getController();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    //sending it to subComponent
    subComponentController.sendRequest(fullFlowerRequest);
    //adding the subcomponent to the container
    subComponentContainer.getChildren().add(subComponentRoot);
  }

  public void toggleSubmitButtonVisibility() {
    submitButton.setVisible(editable);
  }

  /**
   * sends a full flower request to the InfoCardController to be displayed on the InfoCard
   */
  public void sendRequest(FullOfficeRequest fullOfficeRequest) {
    //common fields
    this.fullOfficeRequest = fullOfficeRequest;
    requestType = this.fullOfficeRequest.getRequestType();

    //setting common components with common request info
    setDateSubmittedLabel(fullOfficeRequest.getDateSubmitted().toString());
    setLocationNameLabel(fullOfficeRequest.getLocationName());
    setEmployeeAssignedLabel(fullOfficeRequest.getEmployee());
    setStatusLabel(fullOfficeRequest.getRequestStatus());
    setRequestType(fullOfficeRequest.getRequestType());
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


  /**
   * Sets the requestType information of the InfoCard to the requestType parameter
   *
   * @param requestType
   */
  public void setRequestType(String requestType) {
    setRequestTypeIconImageView(requestType);
  }

  /**
   * Initializes the onclick listener for the edit button
   */
  private void initializeEditButtonOnClickListener() {
    editButton.setOnMouseClicked(event -> {
      toggleEditable();
      editEmployeeAssigned();
      editLocationName();
      toggleEditableForSubComponent();
      editStatus();
      toggleSubmitButtonVisibility();
    });
  }

  /**
   * Toggles the editable state of the InfoCard
   */
  private void toggleEditable() {
    editable = !editable;
  }

  /**
   * Sets the image of the requestTypeIconImageView to the image corresponding to the requestType
   * parameter
   *
   * @param requestType
   */
  private void setRequestTypeIconImageView(String requestType) {
    switch(requestType){
      case "Meal":
        requestTypeIconImageView.setImage(new Image("/edu/wpi/teamb/img/breakfast.png"));
        break;
      case "Conference":
        requestTypeIconImageView.setImage(new Image("/edu/wpi/teamb/img/phone-call.png"));
        break;
      case "Flower":
        requestTypeIconImageView.setImage(new Image("/edu/wpi/teamb/img/flower.png"));
        break;
      case "Office":
        requestTypeIconImageView.setImage(new Image("/edu/wpi/teamb/img/workspace.png"));
        break;
      case "Furniture":
        requestTypeIconImageView.setImage(new Image("edu/wpi/teamb/img/sofa.png"));
        break;
      default:
        requestTypeIconImageView.setImage(new Image("/edu/wpi/teamb/img/helpicon.png"));
        break;
    }
  }

  private void setSubComponentContainer(String requestType){
    //first load the subcomponent
    FXMLLoader subComponentLoader = null;
    try {
      switch (requestType) {
        case "Meal":
          subComponentLoader = new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/MealRequestInfoCardSubComponent.fxml"));
          subComponentRoot = subComponentLoader.load();
          subComponentController = subComponentLoader.getController();
          break;
        default:
          break;
      }
    } catch (IOException e) {
      System.out.println("IOException in setSpecificRequestTypeInfoSubComponent of InfoCardController: " + e.getMessage());
    } catch (NullPointerException e) {
      System.out.println("NullPointerException in setSpecificRequestTypeInfoSubComponent of InfoCardController: " + e.getMessage());
    }
    //then add it to the subcomponent container
    if (subComponentRoot != null){
      subComponentContainer.getChildren().add(subComponentRoot);
    }
  }

  private void toggleEditableForSubComponent() {
    subComponentController.toggleEditMode();
  }
}
