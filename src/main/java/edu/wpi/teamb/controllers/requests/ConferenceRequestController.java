package edu.wpi.teamb.controllers.requests;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.ConferenceRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;

public class ConferenceRequestController {

  @FXML MFXButton btnNext;
  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private MFXDatePicker datePicker;
  @FXML private MFXComboBox<Integer> reservationHour;
  @FXML private MFXComboBox<String> reservationMinute;
  @FXML private MFXComboBox<String> reservationAmPm;
  @FXML private MFXComboBox<String> selectFloorComboBox;
  @FXML private MFXComboBox<String> availableRoomsComboBox;
  @FXML private MFXComboBox<String> cbEmployeesToAssign;

  @FXML private MFXTextField eventNameTextField;
  @FXML private MFXTextField bookingReasonTextField;
  @FXML private MFXButton resetBtn1;
  @FXML private MFXButton cancelBtn1;
  @FXML private MFXButton btnSubmit;
  @FXML private ImageView helpIcon;
  @FXML private MFXFilterComboBox<String> cbLongName;

  private ConferenceRequest conferenceRequest;

  public ConferenceRequestController(){
        this.conferenceRequest = new ConferenceRequest();
    }
  @FXML
  public void initialize() throws IOException, SQLException {
      initializeFields();

      //helpIcon clickHelp setup
      clickHelp();
  }

  private void initializeFields() throws SQLException {

      //Initialize the list of locations to direct request to via dropdown
      ObservableList<String> longNames = FXCollections.observableArrayList();
      longNames.addAll(Repository.getRepository().getAllLongNames());
      cbLongName.setItems(longNames);

      //Dropdown for employee selection
      ObservableList<String> employees =
              FXCollections.observableArrayList();
      employees.addAll(conferenceRequest.getUsernames());
      cbEmployeesToAssign.setItems(employees);

    // Dropdown for reservationHour
    ObservableList<Integer> hoursListItems =
        FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
    reservationHour.setItems(hoursListItems);

    // Dropdown for reservationMinute
    ObservableList<String> minutesListItems = FXCollections.observableArrayList("00", "15", "30", "45");
    reservationMinute.setItems(minutesListItems);

    // Dropdown for reservationAmPm
    ObservableList<String> AmPm = FXCollections.observableArrayList("AM", "PM");
    reservationAmPm.setItems(AmPm);

    // Dropdown for selectFloorComboBox
    ObservableList<String> floorListItems =
        FXCollections.observableArrayList("Lower Floor 1", "Lower Floor 2", "Floor 1", "Floor 2", "Floor 3");
    selectFloorComboBox.setItems(floorListItems);

    // Dropdown for availableRoomsComboBox
    ObservableList<String> availableRoomsList =
        FXCollections.observableArrayList(
            "Room 101",
            "Room 102",
            "Room 103",
            "Room 201",
            "Room 202",
            "Room 203",
            "Room 301",
            "Room 302",
            "Room 303");
    availableRoomsComboBox.setItems(availableRoomsList);

    // changing some properties of the text fields
    // makes cursor visible
    eventNameTextField.setCaretVisible(true);
    bookingReasonTextField.setCaretVisible(true);
    // setting max character limits
    eventNameTextField.textLimitProperty().set(100);
    bookingReasonTextField.textLimitProperty().set(250);
    // setting floating text
    eventNameTextField.setFloatingText("Event Name:");
    bookingReasonTextField.setFloatingText("Reason for Booking:");


  }

  @FXML
  public void clickSubmit() {
    btnSubmit.setOnMouseClicked(
        event -> {
//            String date = datePicker.getText();
//            System.out.println(date);
            //Date daterequested = ;
            String timerequested = "";
            if (reservationAmPm.getText().equals("AM") && reservationHour.getText().equals("12")) {
                timerequested = "00:" + reservationMinute.getText() + ":00";
            } else if (reservationAmPm.getText().equals("AM")) {
                timerequested = reservationHour.getText() + ":" + reservationMinute.getText() + ":00";
            }  else if (reservationAmPm.getText().equals("PM") && reservationHour.getText().equals("12")) {
                timerequested = reservationHour.getText() + ":" + reservationMinute.getText() + ":00";
            } else if (reservationAmPm.getText().equals("PM")) {
                int hour = Integer.parseInt(reservationHour.getText()) + 12;
                timerequested = "" + hour + ":" + reservationMinute.getText() + ":00";
            }

            //Get all fields from request
            String employee = cbEmployeesToAssign.getSelectedItem();
            String floor = selectFloorComboBox.getSelectedItem();
            String roomnumber = availableRoomsComboBox.getSelectedItem();
            String longName = cbLongName.getSelectedItem();
            String requeststatus = ("Pending");
            String daterequested = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
            String requesttype = ("Conference");
            String eventname = eventNameTextField.getText();
            String bookingreason = bookingReasonTextField.getText();
            String timeStamp = daterequested + " " + timerequested;

            //Check for required fields before allowing submittion
            if((employee != null) && ((floor != null) && (roomnumber != null)) && (daterequested != null) && (longName != null) && (eventname != null) && (timerequested != null)){

                //Set the gathered fields into a string array
                String[] output = {employee, floor, roomnumber, requeststatus, requesttype, timeStamp, eventname, bookingreason, longName};
                conferenceRequest.submitRequest(output);
                clickReset();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            } else {

                //If the required fields are not filled, bring up pop-over indicating such
                final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/NotAllFieldsCompleteError.fxml"));
                PopOver popOver = new PopOver();
                popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
                popOver.setArrowSize(0.0);
                try {
                    popOver.setContentNode(popupLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                popOver.show(btnSubmit);
            }
        });
  }

  @FXML
  public void clickReset() {
    resetBtn1.setOnMouseClicked(
        event -> {
          datePicker.setValue(null);
          reservationHour.setValue(12);
          reservationMinute.setValue("00");
          reservationAmPm.setValue("AM");
          selectFloorComboBox.setValue(null);
          availableRoomsComboBox.setValue(null);
          eventNameTextField.setText("");
          bookingReasonTextField.setText("");
          cbEmployeesToAssign.setText("Employees Available");
            cbLongName.clear();
            cbLongName.replaceSelection("All Room Names: ");
        });
  }

  @FXML
  public void clickCancel() {
    cancelBtn1.setOnMouseClicked(
        event -> {
          // first reset all values
          datePicker.setValue(null);
          reservationHour.setValue(12);
          reservationMinute.setValue("00");
          reservationAmPm.setValue("AM");
          selectFloorComboBox.setValue(null);
          availableRoomsComboBox.setValue(null);
          eventNameTextField.setText("");
          bookingReasonTextField.setText("");
          cbLongName.setText("");
          // then switch page
          Navigation.navigate(Screen.HOME);
        });
  }

  @FXML
  public void clickHelp() {
    helpIcon.setOnMouseClicked(
        event -> {
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/ConferenceRequestHelpPopOver.fxml"));
            PopOver popOver = new PopOver();
            popOver.setDetachable(true);
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
            popOver.setArrowSize(0.0);
            try {
                popOver.setContentNode(popupLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            popOver.show(helpIcon);
        });
    //helpIcon.setOnMouseExited(event -> {});
  }

  @FXML
  public void clickExit() {
    System.exit(0);
  }
}
