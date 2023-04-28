package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullOfficeRequest;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EOfficeRequest;
import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;

public class OfficeRequestControllerI implements IRequestController{

    @FXML
    private MFXButton btnSubmit;
    @FXML private MFXButton btnReset;
    @FXML private ImageView helpIcon;
    @FXML private MFXFilterComboBox<String> cbSupplyItems;
    @FXML private MFXFilterComboBox<String> cbSupplyType;
    @FXML private MFXTextField tbSupplyQuantities;
    @FXML private MFXTextField txtFldNotes;
    @FXML private MFXFilterComboBox<String> cbEmployeesToAssign;
    @FXML private MFXFilterComboBox<String> cbLongName;

    private final EOfficeRequest EOfficeRequest;

    public OfficeRequestControllerI(){
        this.EOfficeRequest = new EOfficeRequest();
    }

    @FXML
    public void initialize() throws IOException, SQLException {
        initBtns();
        initializeFields();
    }

    @Override
    public void initBtns() {
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setOnAction(e -> handleReset());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getPracticalLongNames());
        Collections.sort(longNames);
        cbLongName.setItems(longNames);

        //DROPDOWN INITIALIZATION
        ObservableList<String> employees = FXCollections.observableArrayList(EOfficeRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "Unassigned");
        cbEmployeesToAssign.setItems(employees);

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplies = FXCollections.observableArrayList("Pencils", "Pens", "Paper", "Stapler", "Staples", "Tape", "Scissors", "Glue", "Markers", "Highlighters", "Post-It Notes", "Paper Clips", "Binder Clips", "Folders", "Envelopes", "Printer Paper");
        Collections.sort(supplies);
        cbSupplyItems.setItems(supplies);

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplyType = FXCollections.observableArrayList("Office Supplies", "Cleaning Supplies");
        Collections.sort(supplyType);
        cbSupplyType.setItems(supplyType);
    }

    @Override
    public void handleSubmit() {
        if (nullInputs())
            showPopOver();
        else {
            // Get the standard request fields
            EOfficeRequest.setEmployee(cbEmployeesToAssign.getValue());
            EOfficeRequest.setLocationName(cbLongName.getValue());
            EOfficeRequest.setRequestStatus(IRequest.RequestStatus.Pending);
            EOfficeRequest.setNotes(txtFldNotes.getText());

            // Get the office specific fields
            EOfficeRequest.setType(cbSupplyType.getValue());
            EOfficeRequest.setItem(cbSupplyItems.getValue());
            EOfficeRequest.setQuantity(Integer.parseInt(tbSupplyQuantities.getText()));

            if (EOfficeRequest.checkRequestFields() && EOfficeRequest.checkSpecialRequestFields()) {
                String[] output = {
                        EOfficeRequest.getEmployee(),
                        String.valueOf(EOfficeRequest.getRequestStatus()),
                        EOfficeRequest.getLocationName(),
                        EOfficeRequest.getNotes(),
                        EOfficeRequest.getType(),
                        EOfficeRequest.getItem(),
                        Integer.toString(EOfficeRequest.getQuantity())
                };
                EOfficeRequest.submitRequest(output);
                handleReset();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            }
            submissionAlert();
        }
    }

    @Override
    public void handleReset() {
        cbEmployeesToAssign.clear();
        cbSupplyItems.clear();
        cbSupplyType.clear();
        tbSupplyQuantities.clear();
        txtFldNotes.clear();
        cbLongName.clear();
    }

    @Override
    public void handleHelp() {
        final FXMLLoader popupLoader =
                new FXMLLoader(Bapp.class.getResource("views/components/OfficeRequestHelpPopover.fxml"));
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
    public boolean nullInputs(){
        return cbEmployeesToAssign.getValue() == null || cbSupplyItems.getValue() == null || cbSupplyType.getValue() == null || tbSupplyQuantities.getText().isEmpty() || cbLongName.getValue() == null;
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
    public void enterOfficeRequestsEditableMode(FullOfficeRequest fullOfficeRequest, InfoCardController currentInfoCardController) {
        //set the editable fields to the values of the request
        cbEmployeesToAssign.getSelectionModel().selectItem(fullOfficeRequest.getEmployee());
        System.out.println(fullOfficeRequest.getId() + " " + fullOfficeRequest.getItem());
        cbSupplyItems.getSelectionModel().selectItem(fullOfficeRequest.getItem());
        cbSupplyType.getSelectionModel().selectItem(fullOfficeRequest.getType());
        tbSupplyQuantities.setText(Integer.toString(fullOfficeRequest.getQuantity()));
        txtFldNotes.setText(fullOfficeRequest.getNotes());
        cbLongName.getSelectionModel().selectItem(fullOfficeRequest.getLocationName());

        //set the submit button to say update
        btnSubmit.setText("Update");
        //remove the current onAction event
        btnSubmit.setOnAction(null);
        //add a new onAction event that updates the request
        btnSubmit.setOnAction(e -> {
            //set the request fields to the new values
            fullOfficeRequest.setEmployee(cbEmployeesToAssign.getValue());
            fullOfficeRequest.setItem(cbSupplyItems.getValue());
            fullOfficeRequest.setType(cbSupplyType.getValue());
            fullOfficeRequest.setQuantity(Integer.parseInt(tbSupplyQuantities.getText()));
            fullOfficeRequest.setNotes(txtFldNotes.getText());
            fullOfficeRequest.setLocationName(cbLongName.getValue());

            //update the request
            EOfficeRequest.updateOfficeReqeust(fullOfficeRequest);
            //send the fullOfficeRequest to the info card controller
            currentInfoCardController.sendRequest(fullOfficeRequest);
            //close the stage
            ((Stage) btnSubmit.getScene().getWindow()).close();
        });

        //set the cancel and reset button to not be visible
        btnReset.setVisible(false);
    }
}
