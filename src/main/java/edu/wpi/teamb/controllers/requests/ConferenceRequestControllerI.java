package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.EConferenceRequest;
import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;


public class ConferenceRequestControllerI implements IRequestController{

    @FXML private MFXComboBox<Integer> reservationHour;
    @FXML private MFXComboBox<String> reservationMinute;
    @FXML private MFXComboBox<String> reservationAmPm;
    @FXML private MFXDatePicker datePicker;
    @FXML private MFXComboBox<String> cbEmployeesToAssign;
    @FXML private MFXTextField eventNameTextField;
    @FXML private MFXTextField bookingReasonTextField;
    @FXML private MFXComboBox<Integer> cbDuration;
    @FXML private MFXFilterComboBox<String> cbLongName;
    @FXML private MFXTextField tfNotes;
    @FXML private MFXButton resetBtn;
    @FXML private MFXButton cancelBtn;
    @FXML private MFXButton btnSubmit;
    @FXML private ImageView helpIcon;

    private final EConferenceRequest EConferenceRequest;

    public ConferenceRequestControllerI(){
        this.EConferenceRequest = new EConferenceRequest();
    }
    @FXML
    public void initialize() throws IOException, SQLException {
        initBtns();
        initializeFields();
    }

    @Override
    public void initBtns() {
        btnSubmit.setOnAction(e -> handleSubmit());
        resetBtn.setOnAction(e -> handleReset());
        cancelBtn.setOnAction(e -> handleCancel());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        //Initialize the list of locations to direct request to via dropdown
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cbLongName.setItems(longNames);

        //Dropdown for employee selection
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(EConferenceRequest.getUsernames());
        cbEmployeesToAssign.setItems(employees);

        //Dropdown for duration selection
        ObservableList<Integer> duration =
                FXCollections.observableArrayList();
        duration.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        cbDuration.setItems(duration);

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

        // changing some properties of the text fields
        // makes cursor visible
        eventNameTextField.setCaretVisible(true);
        bookingReasonTextField.setCaretVisible(true);
        // setting max character limits
        eventNameTextField.textLimitProperty().set(100);
        bookingReasonTextField.textLimitProperty().set(250);
    }

    @Override
    public void handleSubmit() {
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
        String daterequested = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
        String timeStamp = daterequested + " " + timerequested;

        // Get the standard request fields
        EConferenceRequest.setEmployee(cbEmployeesToAssign.getValue());
        EConferenceRequest.setLocationName(cbLongName.getValue());
        EConferenceRequest.setRequestStatus(IRequest.RequestStatus.Pending);
        EConferenceRequest.setNotes(tfNotes.getText());

        // Get the conference specific fields
        EConferenceRequest.setDateRequested(Timestamp.valueOf(timeStamp));
        EConferenceRequest.setEventName(eventNameTextField.getText());
        EConferenceRequest.setBookingReason(bookingReasonTextField.getText());
        EConferenceRequest.setDuration(cbDuration.getValue());

        //Check for required fields before allowing submittion
        if(EConferenceRequest.checkRequestFields() && EConferenceRequest.checkSpecialRequestFields()){
            //Set the gathered fields into a string array
            String[] output = {EConferenceRequest.getEmployee(),
                    String.valueOf(EConferenceRequest.getRequestStatus()),
                    EConferenceRequest.getLocationName(),
                    EConferenceRequest.getNotes(),
                    String.valueOf(EConferenceRequest.getDateRequested()),
                    EConferenceRequest.getEventName(),
                    EConferenceRequest.getBookingReason(),
                    String.valueOf(EConferenceRequest.getDuration())
            };

            EConferenceRequest.submitRequest(output);
            handleReset();
            Navigation.navigate(Screen.CREATE_NEW_REQUEST);
        } else {
            //If the required fields are not filled, bring up pop-over indicating such
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/NotAllFieldsCompleteError.fxml"));
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
        submissionAlert();
    }

    @Override
    public void handleReset() {
        datePicker.setValue(null);
        reservationHour.setValue(12);
        reservationMinute.setValue("00");
        reservationAmPm.setValue("AM");
        datePicker.setValue(null);
        cbDuration.setValue(null);
        eventNameTextField.setText("");
        bookingReasonTextField.setText("");
        tfNotes.setText("");
        cbEmployeesToAssign.setText("Employees Available");
        cbLongName.clear();
        cbLongName.replaceSelection("All Room Names: ");
    }

    @Override
    public void handleHelp() {
        final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/ConferenceRequestHelpPopOver.fxml"));
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
    }
}
