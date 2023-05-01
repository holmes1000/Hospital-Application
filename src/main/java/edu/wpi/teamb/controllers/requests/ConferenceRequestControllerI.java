package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EConferenceRequest;
import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import edu.wpi.teamb.utils.TimeFormattingHelpers;
import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


public class ConferenceRequestControllerI implements IRequestController{

    @FXML private MFXFilterComboBox<String> reservationHour;
   // @FXML private MFXDatePicker datePicker;
    @FXML private DatePicker dateToReserve;
    @FXML private MFXFilterComboBox<String> cbEmployeesToAssign;
    @FXML private MFXTextField eventNameTextField;
    @FXML private MFXTextField bookingReasonTextField;
    @FXML private MFXFilterComboBox<String> cbDuration;
    @FXML private MFXFilterComboBox<String> cbLongName;
    @FXML private MFXTextField tfNotes;
    @FXML private MFXButton resetBtn;
    @FXML private MFXButton btnSubmit;
    @FXML private SplitPane spSubmit;
    @FXML private ImageView helpIcon;

    private final EConferenceRequest EConferenceRequest;

    public ConferenceRequestControllerI(){
        this.EConferenceRequest = new EConferenceRequest();
    }
    @FXML
    public void initialize() throws IOException, SQLException {
        initBtns();
        initializeFields();
//        datePicker.setStartingYearMonth(YearMonth.from(datePicker.getCurrentDate()));
//        NumberRange<Integer> range = new NumberRange<>(datePicker.getCurrentDate().getYear(), datePicker.getCurrentDate().getYear() + 1);
//        datePicker.setYearsRange(range);
        dateToReserve.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
    }

    @Override
    public void initBtns() {
        spSubmit.setTooltip(new Tooltip("Enter all required fields to submit request"));
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(reservationHour.valueProperty(),
                        dateToReserve.valueProperty(),
                        cbEmployeesToAssign.valueProperty(),
                        eventNameTextField.textProperty(),
                        bookingReasonTextField.textProperty(),
                        cbDuration.valueProperty(),
                        cbLongName.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (reservationHour.getValue() == null ||
                        dateToReserve.getValue() == null ||
                        cbEmployeesToAssign.getValue() == null ||
                        eventNameTextField.getText().isEmpty() ||
                        bookingReasonTextField.getText().isEmpty() ||
                        cbDuration.getValue() == null ||
                        cbLongName.getValue() == null);
            }
        };
        btnSubmit.disableProperty().bind(bb);

        btnSubmit.setTooltip(new Tooltip("Click to submit request"));
        btnSubmit.setOnAction(e -> handleSubmit());
        resetBtn.setTooltip(new Tooltip("Click to reset all fields"));
        resetBtn.setOnAction(e -> handleReset());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        //Initialize the list of locations to direct request to via dropdown
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getLongNameByType("CONF"));
        Collections.sort(longNames);
        cbLongName.setItems(longNames);
        cbLongName.setTooltip(new Tooltip("Select a location to direct the request to"));

        //Dropdown for employee selection
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(EConferenceRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "unassigned");
        cbEmployeesToAssign.setItems(employees);
        cbEmployeesToAssign.setTooltip(new Tooltip("Select an employee to assign the request to"));

        ObservableList<String> duration =
                FXCollections.observableArrayList();
        duration.add(12 + ":00" + " AM");
        duration.add(12 + ":30" + " AM");
        for (int i = 1; i < 12; i++) {
            duration.add(i + ":00" + " AM");
            duration.add(i + ":30" + " AM");
        }
        duration.add(12 + ":00" + " PM");
        duration.add(12 + ":30" + " PM");
        for (int i = 1; i < 12; i++) {
            duration.add(i + ":00" + " PM");
            duration.add(i + ":30" + " PM");
        }
        cbDuration.setItems(duration);
        reservationHour.setItems(duration);
        reservationHour.setTooltip(new Tooltip("Select a start time for the request"));

        // changing some properties of the text fields
        // makes cursor visible
        eventNameTextField.setCaretVisible(true);
        eventNameTextField.setTooltip(new Tooltip("Enter a name for the event"));
        bookingReasonTextField.setCaretVisible(true);
        bookingReasonTextField.setTooltip(new Tooltip("Enter a reason for the booking"));
        // setting max character limits
        eventNameTextField.textLimitProperty().set(100);
        bookingReasonTextField.textLimitProperty().set(250);
        reservationHour.clear();
    }

    @Override
    public void handleSubmit() {
        // Check if the input fields are null
        if (nullInputs()) {
            showPopOver();
        }
        else {
            String startHour = reservationHour.getValue().substring(0, reservationHour.getValue().indexOf(":"));
            String startMinute = reservationHour.getValue().substring(reservationHour.getValue().indexOf(":") + 1, reservationHour.getValue().indexOf(" "));
            String startAmPm = reservationHour.getValue().substring(reservationHour.getValue().indexOf(" ") + 1);
            String timerequestedFormatted = "";
            if (startAmPm.equals("AM") && startHour.equals("12")) {
                timerequestedFormatted = "00:" + startMinute + ":00";
            } else if (startAmPm.equals("AM")) {
                timerequestedFormatted = startHour + ":" + startMinute + ":00";
            }  else if (startAmPm.equals("PM") && startHour.equals("12")) {
                timerequestedFormatted = startHour + ":" + startMinute + ":00";
            } else if (startAmPm.equals("PM")) {
                int hour = Integer.parseInt(startHour) + 12;
                timerequestedFormatted = "" + startHour + ":" + startMinute + ":00";
            }
            String daterequested = dateToReserve.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
            String timeStamp = daterequested + " " + timerequestedFormatted;

            // Get the standard request fields
            EConferenceRequest.setEmployee(cbEmployeesToAssign.getValue());
            EConferenceRequest.setLocationName(cbLongName.getValue());
            EConferenceRequest.setRequestStatus(IRequest.RequestStatus.Pending);
            EConferenceRequest.setNotes(tfNotes.getText());

            // Get the conference specific fields
            EConferenceRequest.setDateRequested(Timestamp.valueOf(timeStamp));
            EConferenceRequest.setEventName(eventNameTextField.getText());
            EConferenceRequest.setBookingReason(bookingReasonTextField.getText());
            EConferenceRequest.setDuration(calcDuration(reservationHour.getValue(), cbDuration.getValue()));

            //Check for required fields before allowing submission
            if (EConferenceRequest.checkRequestFields() && EConferenceRequest.checkSpecialRequestFields()) {
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
            }
            submissionAlert();
        }
    }

    @Override
    public void handleReset() {
        dateToReserve.getEditor().clear();
//        reservationHour.setValue("12:00 AM");
        cbDuration.clear();
        eventNameTextField.clear();
        bookingReasonTextField.clear();
        tfNotes.clear();
        cbEmployeesToAssign.clear();
        cbLongName.clear();
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

    @Override
    public boolean nullInputs() {
        return cbEmployeesToAssign.getValue() == null
                || cbLongName.getValue() == null
                || eventNameTextField.getText().isEmpty()
                || bookingReasonTextField.getText().isEmpty()
                || cbDuration.getValue() == null
                || reservationHour.getValue() == null
                || dateToReserve.getValue() == null;
    }

    @Override
    public void showPopOver() {
        final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/NotAllFieldsCompleteError.fxml"));
        PopOver popOver = new PopOver();
        popOver.setDetachable(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        popOver.setArrowSize(0.0);
        try {
            popOver.setContentNode(popupLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        popOver.show(btnSubmit);
    }

    //functions for editable stage in InfoCardController
    public void enterConferenceRequestEditableMode(FullConferenceRequest fullConferenceRequest, InfoCardController currentInfoCardController) {
        //set the editable fields to the values of the request
        cbEmployeesToAssign.getSelectionModel().selectItem(fullConferenceRequest.getEmployee());
        cbLongName.getSelectionModel().selectItem(fullConferenceRequest.getLocationName());
        tfNotes.setText(fullConferenceRequest.getNotes());
        eventNameTextField.setText(fullConferenceRequest.getEventName());
        bookingReasonTextField.setText(fullConferenceRequest.getBookingReason());
        cbDuration.getSelectionModel().selectItem(calcTime(fullConferenceRequest.getDuration(), fullConferenceRequest.getDateRequested().toLocalDateTime().getHour(), fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute()));
        dateToReserve.setValue(fullConferenceRequest.getDateRequested().toLocalDateTime().toLocalDate());
        int hour1 = fullConferenceRequest.getDateRequested().toLocalDateTime().getHour();
        int minute1 = fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute();
        String am_pm = "AM";
        if (hour1 > 12) {
            hour1 -= 12;
            am_pm = "PM";
        } else if (hour1 == 12) {
            am_pm = "PM";
        } else if (hour1 == 0) {
            hour1 = 12;
        }
        if (minute1 < 10) {
            reservationHour.getSelectionModel().selectItem(String.valueOf(hour1) + ":" + "0" + String.valueOf(minute1) + " " + am_pm);
        } else {
            reservationHour.getSelectionModel().selectItem(String.valueOf(hour1) + ":" + String.valueOf(minute1) + " " + am_pm);
        }
        //String time = (String.valueOf(TimeFormattingHelpers.get24to12Hour(hour1)) + ":" + String.valueOf(fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute()));
        //reservationHour.getSelectionModel().selectItem(time);
        String minutes;
        if (fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute() < 10) {
            minutes = "0" + fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute();
        } else {
            minutes = "" + fullConferenceRequest.getDateRequested().toLocalDateTime().getMinute();
        }

        //set the submit button to say update
        btnSubmit.setText("Update");
        //remove the current onAction event
        btnSubmit.setOnAction(null);
        //add a new onAction event
        btnSubmit.setOnAction(e -> {
            //update all the fields of the fullConferenceRequest
            fullConferenceRequest.setEmployee(cbEmployeesToAssign.getValue());
            fullConferenceRequest.setLocationName(cbLongName.getValue());
            fullConferenceRequest.setNotes(tfNotes.getText());
            fullConferenceRequest.setEventName(eventNameTextField.getText());
            fullConferenceRequest.setBookingReason(bookingReasonTextField.getText());
            fullConferenceRequest.setDateRequested(Timestamp.valueOf(dateToReserve.getValue() + " " + reservationHour.getValue() + ":00"));
            fullConferenceRequest.setDuration(calcDuration(reservationHour.getText(), cbDuration.getValue()));
            String startHour = reservationHour.getValue().substring(0, reservationHour.getValue().indexOf(":"));
            String startMinute = reservationHour.getValue().substring(reservationHour.getValue().indexOf(":") + 1, reservationHour.getValue().indexOf(" "));
            String startAmPm = reservationHour.getValue().substring(reservationHour.getValue().indexOf(" ") + 1);
            String timerequestedFormatted = "";
            if (startAmPm.equals("AM") && startHour.equals("12")) {
                timerequestedFormatted = "00:" + startMinute + ":00";
            } else if (startAmPm.equals("AM")) {
                timerequestedFormatted = startHour + ":" + startMinute + ":00";
            }  else if (startAmPm.equals("PM") && startHour.equals("12")) {
                timerequestedFormatted = startHour + ":" + startMinute + ":00";
            } else if (startAmPm.equals("PM")) {
                int hour = Integer.parseInt(startHour) + 12;
                timerequestedFormatted = "" + hour + ":" + startMinute + ":00";
            }
            String daterequested = dateToReserve.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
            String timeStamp = daterequested + " " + timerequestedFormatted;

            fullConferenceRequest.setDateRequested(Timestamp.valueOf(timeStamp));
            //update the database
            EConferenceRequest.updateConferenceRequest(fullConferenceRequest);
            //close the window
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.close();
            //send the fullConferenceRequest to the info card controller
            currentInfoCardController.sendRequest(fullConferenceRequest);
        });

        //set the cancel and reset buttons to not be visible
        resetBtn.setVisible(false);
    }

    public int calcDuration(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
        int startMinute = Integer.parseInt(startTime.substring(startTime.indexOf(":") + 1, startTime.indexOf(" ")));
        int endHour = Integer.parseInt(endTime.substring(0, endTime.indexOf(":")));
        int endMinute = Integer.parseInt(endTime.substring(endTime.indexOf(":") + 1, endTime.indexOf(" ")));
        if (endHour < startHour) {
            endHour += 12;
        }
        if (endMinute < startMinute) {
            endMinute += 60;
            endHour--;
        }
        int totalMinutes = (endHour - startHour) * 60 + (endMinute - startMinute);
        return totalMinutes;
    }

    public String calcTime(int duration, int startHour, int startMinute) {
        String time = "";
        String startAmPm = "AM";
        if (startHour > 12) {
            startHour -= 12;
            startAmPm = "PM";
        }
        if (duration != 0) {
            int endHour = startHour;
            int endMinute = startMinute + duration;
            if (endMinute >= 60) {
                endHour++;
                endMinute -= 60;
            }
            if (endHour >= 24) {
                endHour -= 24;
            }
            if (endHour > 12) {
                endHour -= 12;
                if (startAmPm == "AM") {
                    startAmPm = "PM";
                } else {
                    startAmPm = "AM";
                }
            }
            if (endMinute < 10) {
                time = endHour + ":0" + endMinute + " " + startAmPm;
            } else {
                time = endHour + ":" + endMinute + " " + startAmPm;
            }
        }
        return time;
    }
}