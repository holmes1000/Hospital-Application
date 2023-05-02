package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullTranslationRequest;
import edu.wpi.teamb.controllers.components.InfoCardController;
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
    @FXML private MFXFilterComboBox<String> cbLanguageSelect;
    @FXML private MFXFilterComboBox<String> cdMedicalInfo;
    @FXML private MFXTextField txtFldNotes;
    @FXML private MFXTextField txtFldMessage;
    @FXML private MFXFilterComboBox<String> cbEmployeesToAssign;
    @FXML private MFXFilterComboBox<String> cbLongName;

    private final edu.wpi.teamb.entities.requests.ETranslationRequest ETranslationRequest; // ETranslationRequest object to be submitted

    public TranslationRequestControllerI() {
        this.ETranslationRequest = new edu.wpi.teamb.entities.requests.ETranslationRequest();
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
                        cdMedicalInfo.valueProperty(),
                        cbLongName.valueProperty(),
                        cbEmployeesToAssign.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (cbLanguageSelect.getValue() == null ||
                        cdMedicalInfo.getValue() == null ||
                        cbLongName.getValue() == null ||
                        cbEmployeesToAssign.getValue() == null);
            }
        };
        btnSubmit.disableProperty().bind(bb);

        btnSubmit.setTooltip(new Tooltip("Click to submit request"));
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setTooltip(new Tooltip("Click to reset the form"));
        btnReset.setOnAction(e -> handleReset());
    }

    @Override
    public void initializeFields() throws SQLException {
        //Initialize the list of locations to direct request to via dropdown
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getPracticalLongNames());
        Collections.sort(longNames);
        cbLongName.setItems(longNames);
        cbLongName.setTooltip(new Tooltip("Select a location to direct the request to"));

        //Set types of languages
        ObservableList<String> languages = FXCollections.observableArrayList("Spanish", "Mandarin", "French", "Vietnamese", "German", "Other");
        Collections.sort(languages);
        cbLanguageSelect.setItems(languages);
        cbLanguageSelect.setTooltip(new Tooltip("Select a language to translate to"));

        //Set importance of translation medically
        ObservableList<String> importance = FXCollections.observableArrayList("related to treatment","other");
        Collections.sort(importance);
        cdMedicalInfo.setItems(importance);
        cdMedicalInfo.setTooltip(new Tooltip("It's important to clarify if the translation is medically related or not"));


        //todo: fix locations

        //Set list of employees
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(ETranslationRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "unassigned");
        cbEmployeesToAssign.setItems(employees);
        cbEmployeesToAssign.setTooltip(new Tooltip("Select an employee to assign the request to"));
    }

    @Override
    public void handleSubmit() {
        if (nullInputs())
            showPopOver();
        else {
            // Get the standard request fields
            ETranslationRequest.setEmployee(cbEmployeesToAssign.getValue());
            ETranslationRequest.setLocationName(cbLongName.getValue());
            ETranslationRequest.setRequestStatus(IRequest.RequestStatus.Pending);
            ETranslationRequest.setNotes(txtFldNotes.getText());

            // Get the conference specific fields
            ETranslationRequest.setLanguageType(cbLanguageSelect.getValue());
            ETranslationRequest.setMedicalInNature(cdMedicalInfo.getValue());

            //Check for required fields before allowing submittion
            if (ETranslationRequest.checkRequestFields() && ETranslationRequest.checkSpecialRequestFields()) {
                String[] output = {
                        ETranslationRequest.getEmployee(),
                        String.valueOf(ETranslationRequest.getRequestStatus()),
                        ETranslationRequest.getLocationName(),
                        ETranslationRequest.getNotes(),
                        ETranslationRequest.getLanguageType(),
                        ETranslationRequest.getMedicalInNature(),
                };
                ETranslationRequest.submitRequest(output);
                handleReset();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            }
            submissionAlert();
        }
    }

    @Override
    public void handleReset() {
        cbLanguageSelect.clear();
        cdMedicalInfo.clear();
        txtFldNotes.clear();
        cbEmployeesToAssign.clear();
        cbLongName.clear();
    }


    @Override
    public boolean nullInputs() {
        return cbLanguageSelect.getValue() == null
                || cdMedicalInfo.getValue() == null
                || cbEmployeesToAssign.getValue() == null
                || cbLongName.getValue() == null;
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
    public void enterTranslationRequestEditableMode(FullTranslationRequest fullTranslationRequest, InfoCardController currentInfoCardController) {
        //set the editable fields to the values of the request
        cbLanguageSelect.getSelectionModel().selectItem(fullTranslationRequest.getLanguageType());
        cdMedicalInfo.getSelectionModel().selectItem(fullTranslationRequest.getMedical());
        txtFldNotes.setText(fullTranslationRequest.getNotes());
        cbEmployeesToAssign.getSelectionModel().selectItem(fullTranslationRequest.getEmployee());
        cbLongName.getSelectionModel().selectItem(fullTranslationRequest.getLocationName());

        //set the submit button to say update
        btnSubmit.setText("Update");
        //remove the current onAction event
        btnSubmit.setOnAction(null);
        //add a new onAction event
        btnSubmit.setOnAction(e -> {
            //set the request fields to the new values
            fullTranslationRequest.setLanguageType(cbLanguageSelect.getValue());
            fullTranslationRequest.setMedical(cdMedicalInfo.getValue());
            fullTranslationRequest.setNotes(txtFldNotes.getText());
            fullTranslationRequest.setEmployee(cbEmployeesToAssign.getValue());
            fullTranslationRequest.setLocationName(cbLongName.getValue());

            //update the request
            ETranslationRequest.updateTranslationRequest(fullTranslationRequest);
            //send the fullConferenceRequest to the info card controller
            currentInfoCardController.sendRequest(fullTranslationRequest);
            //close the stage
            ((Stage) btnSubmit.getScene().getWindow()).close();
        });
        //set the reset and cancel buttons to not be visible
        btnReset.setVisible(false);
    }
}
