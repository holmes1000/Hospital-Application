package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.FullConferenceRequest;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ConferenceRequestInfoCardSubComponentController implements IInfoCardSubComponentController {
    @FXML public AnchorPane conferenceRequestInfoSubComponentAnchorPane;
    @FXML public Label reserveDateLabel;
    @FXML public Label eventNameLabel;
    @FXML public Label bookingReasonLabel;
    private boolean editable;
    //edit components
    private MFXDatePicker reserveDateMFXDatePicker;
    private MFXTextField eventNameTextField;
    private MFXTextField bookingReasonTextField;

    @FXML public void initialize() throws IOException {
        editable = false;
    }

    @Override
    public void toggleEditMode() {
        toggleEditable();
        editDateRequested();
        editEventName();
        editBookingReason();
    }

    @Override
    public void sendRequest(Object request) {
        if (request instanceof FullConferenceRequest fullConferenceRequest) {
            setReserveDateLabel(fullConferenceRequest.getDateRequested().toString());
            setEventNameLabel(fullConferenceRequest.getEventName());
            setBookingReasonLabel(fullConferenceRequest.getBookingReason());
        }
    }

    private void editDateRequested() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = reserveDateLabel.getLayoutX();
            double labelYCoord = reserveDateLabel.getLayoutY();

            //create a combo box if null
            if(reserveDateMFXDatePicker == null) {
                reserveDateMFXDatePicker = new MFXDatePicker();
            }
            //set the location of the combo box
            int heightOffset = -10;
            reserveDateMFXDatePicker.setLayoutX(labelXCoord);
            reserveDateMFXDatePicker.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = reserveDateLabel.getWidth();
            double labelHeight = reserveDateLabel.getHeight();

            //assign the dimensions to the combo box
            reserveDateMFXDatePicker.setPrefWidth(labelWidth);
//            orderFromComboBox.setPrefHeight();

            //replacement of the label with the combo box
            int index = conferenceRequestInfoSubComponentAnchorPane.getChildren().indexOf(reserveDateLabel);
            conferenceRequestInfoSubComponentAnchorPane.getChildren().add(index, reserveDateMFXDatePicker);

            //set the combo box text to the current order from text
            reserveDateMFXDatePicker.setText(reserveDateLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            reserveDateMFXDatePicker.toFront();
        } else {
            conferenceRequestInfoSubComponentAnchorPane.getChildren().remove(reserveDateMFXDatePicker);
            reserveDateMFXDatePicker = null;
        }
    }

    private void editEventName() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = eventNameLabel.getLayoutX();
            double labelYCoord = eventNameLabel.getLayoutY();

            //create a combo box if null
            if(eventNameTextField == null) {
                eventNameTextField = new MFXTextField();
            }
            //set the location of the combo box
            int heightOffset = -10;
            eventNameTextField.setLayoutX(labelXCoord);
            eventNameTextField.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = eventNameLabel.getWidth();
            double labelHeight = eventNameLabel.getHeight();

            //assign the dimensions to the combo box
            eventNameTextField.setPrefWidth(300);
//            orderFromComboBox.setPrefHeight();

            //replacement of the label with the combo box
            int index = conferenceRequestInfoSubComponentAnchorPane.getChildren().indexOf(eventNameLabel);
            conferenceRequestInfoSubComponentAnchorPane.getChildren().add(index, eventNameTextField);

            //set the combo box text to the current order from text
            eventNameTextField.setText(eventNameLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            eventNameTextField.toFront();
        } else {
            conferenceRequestInfoSubComponentAnchorPane.getChildren().remove(eventNameTextField);
            eventNameTextField = null;
        }
    }

    private void editBookingReason() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = bookingReasonLabel.getLayoutX();
            double labelYCoord = bookingReasonLabel.getLayoutY();

            //create a combo box if null
            if(bookingReasonTextField == null) {
                bookingReasonTextField = new MFXTextField();
            }
            //set the location of the combo box
            int heightOffset = 0;
            bookingReasonTextField.setLayoutX(labelXCoord);
            bookingReasonTextField.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = bookingReasonLabel.getWidth();
            double labelHeight = bookingReasonLabel.getHeight();

            //assign the dimensions to the combo box
            bookingReasonTextField.setPrefWidth(300);
//            bookingReasonTextField.setPrefHeight(300);

            //replacement of the label with the combo box
            int index = conferenceRequestInfoSubComponentAnchorPane.getChildren().indexOf(bookingReasonLabel);
            conferenceRequestInfoSubComponentAnchorPane.getChildren().add(index, bookingReasonTextField);

            //set the combo box text to the current order from text
            bookingReasonTextField.setText(bookingReasonLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            bookingReasonTextField.toFront();
        } else {
            conferenceRequestInfoSubComponentAnchorPane.getChildren().remove(bookingReasonTextField);
            bookingReasonTextField = null;
        }
    }

    public void setReserveDateLabel(String date) {
        reserveDateLabel.setText(date);
    }

    public void setEventNameLabel(String eventName) {
        eventNameLabel.setText(eventName);
    }

    public void setBookingReasonLabel(String bookingReason) {
        bookingReasonLabel.setText(bookingReason);
    }

    private void toggleEditable() {
        editable = !editable;
    }
}
