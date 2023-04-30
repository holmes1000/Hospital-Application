package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullFlowerRequest;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EFlowerRequest;
import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;


public class TranslationRequestControllerI implements IRequestController {
    @FXML
    private MFXButton btnSubmit;
    @FXML private SplitPane spSubmit;
    @FXML private MFXButton btnReset;
    @FXML private ImageView helpIcon;
    @FXML private MFXFilterComboBox<String> cbLanguageSelect;
    @FXML private MFXFilterComboBox<String> cdAvailableColor;
    @FXML private MFXFilterComboBox<String> cdAvailableType;
    @FXML private MFXTextField txtFldNotes;
    @FXML private MFXTextField txtFldMessage;
    @FXML private MFXFilterComboBox<String> cbEmployeesToAssign;
    @FXML private MFXFilterComboBox<String> cbLongName;

    private final edu.wpi.teamb.entities.requests.ETranslationRequest ETranslationRequest; // ETranslationRequest object to be submitted

    public TranslationRequestController() {
        this.ETranslationRequest = new ETranslationRequest();
    }

    @FXML
    public void initialize() throws IOException, SQLException {
        initBtns();
        initializeFields();
    }

    @Override
    public void initBtns() {
        spSubmit.setTooltip(new Tooltip("Enter all required fields to submit request"));
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(cbLanguageSelect.valueProperty(),
                        cdAvailableColor.valueProperty(),
                        cdAvailableType.valueProperty(),
                        cbLongName.valueProperty(),
                        cbEmployeesToAssign.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (cbLanguageSelect.getValue() == null ||
                        cdAvailableColor.getValue() == null ||
                        cdAvailableType.getValue() == null ||
                        cbLongName.getValue() == null ||
                        cbEmployeesToAssign.getValue() == null);
            }
        };
        btnSubmit.disableProperty().bind(bb);

        btnSubmit.setTooltip(new Tooltip("Click to submit request"));
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setTooltip(new Tooltip("Click to reset the form"));
        btnReset.setOnAction(e -> handleReset());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        //Initialize the list of locations to direct request to via dropdown
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getPracticalLongNames());
        Collections.sort(longNames);
        cbLongName.setItems(longNames);
        cbLongName.setTooltip(new Tooltip("Select a location to direct the request to"));

        //Set types of flowers
        ObservableList<String> flowers = FXCollections.observableArrayList("Rose", "Tulip", "Daisy", "Lily", "Sunflower");
        Collections.sort(flowers);
        cbLanguageSelect.setItems(flowers);
        cbLanguageSelect.setTooltip(new Tooltip("Select a type of flower"));

        //Set colors of flowers
        ObservableList<String> colors = FXCollections.observableArrayList("Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "White", "Black", "Brown");
        Collections.sort(colors);
        cdAvailableColor.setItems(colors);
        cdAvailableColor.setTooltip(new Tooltip("Select a color of flower"));


        //Set delivery types
        ObservableList<String> deliveryType = FXCollections.observableArrayList("Bouquet", "Single Flower", "Vase");
        Collections.sort(deliveryType);
        cdAvailableType.setItems(deliveryType);
        cdAvailableType.setTooltip(new Tooltip("Select a type of delivery"));
        //todo: fix locations

        //Set list of employees
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(EFlowerRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "Unassigned");
        cbEmployeesToAssign.setItems(employees);
        cbEmployeesToAssign.setTooltip(new Tooltip("Select an employee to assign the request to"));
    }

    @Override
    public void handleSubmit() {
        if (nullInputs())
            showPopOver();
        else {
            // Get the standard request fields
            EFlowerRequest.setEmployee(cbEmployeesToAssign.getValue());
            EFlowerRequest.setLocationName(cbLongName.getValue());
            EFlowerRequest.setRequestStatus(IRequest.RequestStatus.Pending);
            EFlowerRequest.setNotes(txtFldNotes.getText());

            // Get the conference specific fields
            EFlowerRequest.setFlowerType(cbLanguageSelect.getValue());
            EFlowerRequest.setColor(cdAvailableColor.getValue());
            EFlowerRequest.setSize(cdAvailableType.getValue());
            EFlowerRequest.setMessage(txtFldMessage.getText());

            //Check for required fields before allowing submittion
            if (EFlowerRequest.checkRequestFields() && EFlowerRequest.checkSpecialRequestFields()) {
                String[] output = {
                        EFlowerRequest.getEmployee(),
                        String.valueOf(EFlowerRequest.getRequestStatus()),
                        EFlowerRequest.getLocationName(),
                        EFlowerRequest.getNotes(),
                        EFlowerRequest.getFlowerType(),
                        EFlowerRequest.getColor(),
                        EFlowerRequest.getSize(),
                        EFlowerRequest.getMessage()
                };
                EFlowerRequest.submitRequest(output);
                handleReset();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            }
            submissionAlert();
        }
    }

    @Override
    public void handleReset() {
        cbLanguageSelect.clear();
        cdAvailableColor.clear();
        cdAvailableType.clear();
        txtFldMessage.clear();
        txtFldNotes.clear();
        cbEmployeesToAssign.clear();
        cbLongName.clear();
    }

    @Override
    public void handleHelp() {
        final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/FlowerRequestHelpPopOver.fxml"));
        PopOver popOver = new PopOver();
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
        return cbLanguageSelect.getValue() == null || cdAvailableColor.getValue() == null || cdAvailableType.getValue() == null || txtFldMessage.getText().isEmpty() || txtFldNotes.getText().isEmpty() || cbEmployeesToAssign.getValue() == null || cbLongName.getValue() == null;
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
    public void enterFlowerRequestEditableMode(FullFlowerRequest fullFlowerRequest, InfoCardController currentInfoCardController) {
        //set the editable fields to the values of the request
        cbLanguageSelect.getSelectionModel().selectItem(fullFlowerRequest.getFlowerType());
        cdAvailableColor.getSelectionModel().selectItem(fullFlowerRequest.getColor());
        cdAvailableType.getSelectionModel().selectItem(fullFlowerRequest.getSize());
        txtFldMessage.setText(fullFlowerRequest.getMessage());
        txtFldNotes.setText(fullFlowerRequest.getNotes());
        cbEmployeesToAssign.getSelectionModel().selectItem(fullFlowerRequest.getEmployee());
        cbLongName.getSelectionModel().selectItem(fullFlowerRequest.getLocationName());

        //set the submit button to say update
        btnSubmit.setText("Update");
        //remove the current onAction event
        btnSubmit.setOnAction(null);
        //add a new onAction event
        btnSubmit.setOnAction(e -> {
            //set the request fields to the new values
            fullFlowerRequest.setFlowerType(cbLanguageSelect.getValue());
            fullFlowerRequest.setColor(cdAvailableColor.getValue());
            fullFlowerRequest.setSize(cdAvailableType.getValue());
            fullFlowerRequest.setMessage(txtFldMessage.getText());
            fullFlowerRequest.setNotes(txtFldNotes.getText());
            fullFlowerRequest.setEmployee(cbEmployeesToAssign.getValue());
            fullFlowerRequest.setLocationName(cbLongName.getValue());

            //update the request
            EFlowerRequest.updateFlowerRequest(fullFlowerRequest);
            //send the fullConferenceRequest to the info card controller
            currentInfoCardController.sendRequest(fullFlowerRequest);
            //close the stage
            ((Stage) btnSubmit.getScene().getWindow()).close();
        });
        //set the reset and cancel buttons to not be visible
        btnReset.setVisible(false);
    }
}
