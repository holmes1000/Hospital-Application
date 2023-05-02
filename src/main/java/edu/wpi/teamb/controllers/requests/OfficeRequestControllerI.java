package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullOfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.Alert;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EOfficeRequest;
import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
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
import java.sql.Timestamp;
import java.util.Collections;

public class OfficeRequestControllerI implements IRequestController {

    @FXML
    private MFXButton btnSubmit;
    @FXML
    private SplitPane spSubmit;
    @FXML
    private MFXButton btnReset;
    @FXML
    private ImageView helpIcon;
    @FXML
    private MFXFilterComboBox<String> cbSupplyItems;
    @FXML
    private MFXFilterComboBox<String> cbSupplyType;
    @FXML
    private MFXTextField tbSupplyQuantities;
    @FXML
    private MFXTextField txtFldNotes;
    @FXML
    private MFXFilterComboBox<String> cbEmployeesToAssign;
    @FXML
    private MFXFilterComboBox<String> cbLongName;

    private final EOfficeRequest EOfficeRequest;

    public OfficeRequestControllerI() {
        this.EOfficeRequest = new EOfficeRequest();
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
                super.bind(cbSupplyItems.valueProperty(),
                        cbSupplyType.valueProperty(),
                        tbSupplyQuantities.textProperty(),
                        txtFldNotes.textProperty(),
                        cbEmployeesToAssign.valueProperty(),
                        cbLongName.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (cbSupplyItems.getValue() == null
                        || cbSupplyType.getValue() == null
                        || tbSupplyQuantities.getText().isEmpty()
                        || txtFldNotes.getText().isEmpty()
                        || cbEmployeesToAssign.getValue() == null
                        || cbLongName.getValue() == null);
            }
        };
        btnSubmit.disableProperty().bind(bb);

        btnSubmit.setTooltip(new Tooltip("Click to submit request"));
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setTooltip(new Tooltip("Click to reset all fields"));
        btnReset.setOnAction(e -> handleReset());
        btnReset.setDisable(true);
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            btnReset.setDisable(false);
        };
        cbEmployeesToAssign.textProperty().addListener(changeListener);
        cbLongName.textProperty().addListener(changeListener);
        cbSupplyItems.textProperty().addListener(changeListener);
        cbSupplyType.textProperty().addListener(changeListener);
        tbSupplyQuantities.textProperty().addListener(changeListener);
        txtFldNotes.textProperty().addListener(changeListener);
    }

    @Override
    public void initializeFields() throws SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getPracticalLongNames());
        Collections.sort(longNames);
        cbLongName.setItems(longNames);
        cbLongName.setTooltip(new Tooltip("Select a location"));
        //DROPDOWN INITIALIZATION
        ObservableList<String> employees = FXCollections.observableArrayList(EOfficeRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "unassigned");
        cbEmployeesToAssign.setItems(employees);
        cbEmployeesToAssign.setTooltip(new Tooltip("Select an employee to assign the request to"));

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplies = FXCollections.observableArrayList("Please select a type of supply to view the available options.");
        Collections.sort(supplies);
        cbSupplyItems.setItems(supplies);

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplyType = FXCollections.observableArrayList("Cleaning Supplies", "Electronics Supplies", "Office Supplies");
        Collections.sort(supplyType);
        //cbSupplyType.setItems(supplyType);
        initComboBoxChangeListeners();
    }


    private void initComboBoxChangeListeners() {
//        cbSupplyItems.setVisible(false);
        cbSupplyItems.setTooltip(new Tooltip("Select a supply item"));
        cbSupplyType.setTooltip(new Tooltip("Select a supply category"));
        cbSupplyType.getItems().addAll("Cleaning Supplies", "Electronics Supplies", "Office Supplies");
        cbSupplyType.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.equals("Office Supplies")) {
                        cbSupplyItems.getItems().clear();
                        ObservableList<String> officeSupplies = FXCollections.observableArrayList("Pencils", "Pens", "Paper", "Stapler", "Staples", "Tape", "Scissors", "Glue", "Markers", "Highlighters", "Post-It Notes", "Paper Clips", "Binder Clips", "Folders", "Envelopes", "Printer Paper");
                        Collections.sort(officeSupplies);
                        cbSupplyItems.getItems().addAll(officeSupplies);
//                        cbSupplyItems.setVisible(true);
                    } else if (newValue.equals("Cleaning Supplies")) {
                        cbSupplyItems.getItems().clear();
                        ObservableList<String> cleaningSupplies = FXCollections.observableArrayList("Bleach", "Disinfectant Wipes", "Hand Sanitizer", "Soap", "Toilet Paper", "Paper Towels", "Trash Bags", "Dish Soap", "Sponges", "Dishwasher Detergent", "Laundry Detergent", "Fabric Softener", "Dryer Sheets", "Broom", "Mop", "Vacuum", "Duster", "Dustpan", "Trash Can", "Trash Can Liners", "Air Freshener", "Glass Cleaner", "All-Purpose Cleaner", "Furniture Polish", "Squeegee", "Toilet Brush", "Plunger", "Rubber Gloves", "Bucket");
                        Collections.sort(cleaningSupplies);
                        cbSupplyItems.getItems().addAll(cleaningSupplies);
//                        cbSupplyItems.setVisible(true);
                    } else if (newValue.equals("Electronics Supplies")){
                        cbSupplyItems.getItems().clear();
                        ObservableList<String> electronicSupplies = FXCollections.observableArrayList("Batteries", "Light Bulbs", "Extension Cords", "Power Strips", "Surge Protectors", "Ethernet Cables", "HDMI Cables", "USB Cables", "Phone Chargers", "Laptop Chargers", "Headphones", "Earbuds", "Speakers", "Microphone", "Webcam", "Printer Ink", "Printer Toner", "Printer Paper");
                        Collections.sort(electronicSupplies);
                        cbSupplyItems.getItems().addAll(electronicSupplies);
                    } else {
                        cbSupplyItems.getItems().clear();
//                        cbSupplyItems.setVisible(false);
                    }
                });
    }


    @Override
    public void handleSubmit() {
        if (nullInputs()) {
            showPopOver();
        }else if(tbSupplyQuantities.getText().replaceAll("[a-zA-Z]", "").length() != 0){
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
                alertEmployee(cbEmployeesToAssign.getValue());
                handleReset();
            }
            submissionAlert();
        } else {
            tbSupplyQuantities.clear();
            tbSupplyQuantities.setTooltip(new Tooltip("Please only enter integer values"));
        }
    }

    /**
     * Grabs the current employee that is referred to in the newly made request and alerts them of this
     * @param employee
     */
    public void alertEmployee(String employee){
        Alert newAlert = new Alert();
        newAlert.setTitle("New Task Assigned");
        newAlert.setDescription("You have been assigned a new office request to complete.");
        newAlert.setEmployee(employee);
        newAlert.setCreated_at(new Timestamp(System.currentTimeMillis()));
        Repository.getRepository().addAlert(newAlert);
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
    public boolean nullInputs(){
        return cbEmployeesToAssign.getValue() == null
                || cbSupplyItems.getValue() == null
                || cbSupplyType.getValue() == null
                || tbSupplyQuantities.getText().isEmpty()
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
