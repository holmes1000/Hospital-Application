package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullFurnitureRequest;
import edu.wpi.teamb.DBAccess.ORMs.Alert;
import edu.wpi.teamb.controllers.components.InfoCardController;
import edu.wpi.teamb.entities.requests.EFurnitureRequest;
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
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class FurnitureRequestControllerI implements IRequestController{

    @FXML
    private MFXButton btnSubmit;
    @FXML
    private SplitPane spSubmit;
    @FXML
    private MFXButton btnReset;
    @FXML
    private ImageView helpIcon;
    @FXML
    private MFXFilterComboBox<String> cbAvailableFurniture;
    @FXML
    private MFXFilterComboBox<String> cdAvailableModels;
    @FXML
    private MFXFilterComboBox<String> cdAssembly;
    @FXML
    private MFXTextField txtFldNotes;

    @FXML
    private MFXFilterComboBox<String> cbEmployeesToAssign;

    @FXML private MFXFilterComboBox<String> cbLongName;

    @FXML private MFXFilterComboBox<String> cbChangeStatus;

    private edu.wpi.teamb.entities.requests.EFurnitureRequest EFurnitureRequest;

    public FurnitureRequestControllerI() {
        this.EFurnitureRequest = new EFurnitureRequest();
    }

    @FXML
    public void initialize() throws IOException, SQLException {
        initializeFields();
        initBtns();
    }

    @Override
    public void initBtns() {
        spSubmit.setTooltip(new Tooltip("Enter all required fields to submit request"));
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(cbAvailableFurniture.valueProperty(),
                        cdAvailableModels.valueProperty(),
                        cdAssembly.valueProperty(),
                        cbLongName.valueProperty(),
                        cbEmployeesToAssign.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (cbAvailableFurniture.getValue() == null ||
                        cdAvailableModels.getValue() == null ||
                        cdAssembly.getValue() == null ||
                        cbLongName.getValue() == null ||
                        cbEmployeesToAssign.getValue() == null);
            }
        };
        btnSubmit.disableProperty().bind(bb);

        btnSubmit.setTooltip(new Tooltip("Click to submit request"));
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setTooltip(new Tooltip("Click to reset fields"));
        btnReset.setOnAction(e -> handleReset());
        btnReset.setDisable(true);
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            btnReset.setDisable(false);
        };
        cbEmployeesToAssign.textProperty().addListener(changeListener);
        cbLongName.textProperty().addListener(changeListener);
        cbAvailableFurniture.textProperty().addListener(changeListener);
        cdAvailableModels.textProperty().addListener(changeListener);
        cdAssembly.textProperty().addListener(changeListener);
    }

    @Override
    public void initializeFields() throws SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getPracticalLongNames());
        Collections.sort(longNames);
        cbLongName.setItems(longNames);
        cbLongName.setTooltip(new Tooltip("Select a location to direct the request to"));

        //Set list of furniture
        ObservableList<String> furniture = FXCollections.observableArrayList("Chair", "Table", "Bed");
        Collections.sort(furniture);
        cbAvailableFurniture.setItems(furniture);
        cbAvailableFurniture.setTooltip(new Tooltip("Select the type of furniture you want"));

        //Set list of models
        ObservableList<String> models = FXCollections.observableArrayList("Please select a type of furniture to view the available models.");
        Collections.sort(models);
        cdAvailableModels.setItems(models);
        cdAvailableModels.setTooltip(new Tooltip("Select the model of furniture you want"));

        //Set list of assembly options
        ObservableList<String> assembly = FXCollections.observableArrayList("No", "Yes");
        Collections.sort(assembly);
        cdAssembly.setItems(assembly);
        cdAssembly.setTooltip(new Tooltip("Select whether you want the furniture assembled"));

        //Set list of employees
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(EFurnitureRequest.getUsernames());
        Collections.sort(employees);
        employees.add(0, "unassigned");
        cbEmployeesToAssign.setItems(employees);
        cbEmployeesToAssign.setTooltip(new Tooltip("Select an employee to assign the request to"));
        initComboBoxChangeListeners();

        txtFldNotes.setTooltip(new Tooltip("Enter any special instructions"));
        ObservableList<String> statuses = FXCollections.observableArrayList("In-Progress", "Pending", "Completed");
        Collections.sort(statuses);
        cbChangeStatus.setItems(statuses);
    }

    private void initComboBoxChangeListeners() {
//        cdAvailableModels.setVisible(true);
        ObservableList<String> furniture = FXCollections.observableArrayList("Chair", "Table", "Bed");
        Collections.sort(furniture);
        cbAvailableFurniture.getItems().clear();
        cbAvailableFurniture.getItems().addAll(furniture);
        cbAvailableFurniture.valueProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    if (newValue.equals("Chair")) {
                        cdAvailableModels.getItems().clear();
                        ObservableList<String> models = FXCollections.observableArrayList("Sofa", "Armchair", "Recliner", "Desk Chair", "Stool");
                        Collections.sort(models);
                        cdAvailableModels.getItems().addAll(models);
                        cdAvailableModels.setVisible(true);
                    } else if (newValue.equals("Table")) {
                        cdAvailableModels.getItems().clear();
                        ObservableList<String> models = FXCollections.observableArrayList("Writing Desk", "Coffee Table", "Dining Table", "End Table", "Nightstand", "Computer Desk", "Dressing Table");
                        Collections.sort(models);
                        cdAvailableModels.getItems().addAll(models);
                        cdAvailableModels.setVisible(true);
                    } else if (newValue.equals("Bed")) {
                        cdAvailableModels.getItems().clear();
                        ObservableList<String> models = FXCollections.observableArrayList("Sofa Bed", "Futon", "Air Mattress", "Baby Cot", "Medical Bed", "Camp Bed");
                        Collections.sort(models);
                        cdAvailableModels.getItems().addAll(models);
                        cdAvailableModels.setVisible(true);
                    }
                })
        );
    }

    @Override
    public void handleSubmit() {
        if (nullInputs())
            showPopOver();
        else {
            // Get the standard request fields
            EFurnitureRequest.setEmployee(cbEmployeesToAssign.getValue());
            EFurnitureRequest.setLocationName(cbLongName.getValue());
            EFurnitureRequest.setRequestStatus(IRequest.RequestStatus.Pending);
            EFurnitureRequest.setNotes(txtFldNotes.getText());

            // Get the conference specific fields
            EFurnitureRequest.setFurnitureType(cbAvailableFurniture.getValue());
            EFurnitureRequest.setModel(cdAvailableModels.getValue());
            EFurnitureRequest.setAssembly(cdAssembly.getValue());

            if (EFurnitureRequest.checkRequestFields() && EFurnitureRequest.checkSpecialRequestFields()) {
                String[] output = {
                        EFurnitureRequest.getEmployee(),
                        String.valueOf(EFurnitureRequest.getRequestStatus()),
                        EFurnitureRequest.getLocationName(),
                        EFurnitureRequest.getNotes(),
                        EFurnitureRequest.getFurnitureType(),
                        EFurnitureRequest.getModel(),
                        String.valueOf(stringToBoolean(EFurnitureRequest.getAssembly()))
                };
                EFurnitureRequest.submitRequest(output);
                handleReset();
            }
            submissionAlert();
            alertEmployee(EFurnitureRequest.getEmployee());
        }
    }

    /**
     * Grabs the current employee that is referred to in the newly made request and alerts them of this
     * @param employee
     */
    public void alertEmployee(String employee){
        Alert newAlert = new Alert();
        newAlert.setTitle("New Task Assigned");
        newAlert.setDescription("You have been assigned a new furniture request to complete.");
        newAlert.setEmployee(employee);
        newAlert.setCreated_at(new Timestamp(System.currentTimeMillis()));
        Repository.getRepository().addAlert(newAlert);
    }


    @Override
    public void handleReset() {
        cbAvailableFurniture.clear();
        cdAvailableModels.clear();
        cdAssembly.clear();
        txtFldNotes.clear();
        cbEmployeesToAssign.clear();
        cbLongName.clear();
    }


    public boolean stringToBoolean(String assembly) {
        if (assembly.equals("Yes")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean nullInputs() {
        return cbAvailableFurniture.getValue() == null
                || cdAvailableModels.getValue() == null
                || cdAssembly.getValue() == null
                || cbEmployeesToAssign.getValue() == null
                || cbLongName.getValue() == null
                || cbChangeStatus.getSelectionModel() == null;
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
    public void enterFurnitureRequestEditableMode(FullFurnitureRequest fullFurnitureRequest, InfoCardController currentInfoCardController) {
        //set the editable fields to the values of the request
        cbAvailableFurniture.getSelectionModel().selectItem(fullFurnitureRequest.getType());
        //set the furniture types so that edit page does not crash
        if (cbAvailableFurniture.getSelectionModel().getSelectedItem().equals("Chair")) {
            cdAvailableModels.getItems().clear();
            cdAvailableModels.getSelectionModel().clearSelection();
            ObservableList<String> models = FXCollections.observableArrayList("Sofa", "Armchair", "Recliner", "Desk Chair", "Stool");
            Collections.sort(models);
            cdAvailableModels.getItems().addAll(models);
            cdAvailableModels.setVisible(true);
        } else if (cbAvailableFurniture.getSelectionModel().getSelectedItem().equals("Table")) {
            cdAvailableModels.getItems().clear();
            cdAvailableModels.getSelectionModel().clearSelection();
            ObservableList<String> models = FXCollections.observableArrayList("Writing Desk", "Coffee Table", "Dining Table", "End Table", "Nightstand", "Computer Desk", "Dressing Table");
            Collections.sort(models);
            cdAvailableModels.getItems().addAll(models);
            cdAvailableModels.setVisible(true);
        } else if (cbAvailableFurniture.getSelectionModel().getSelectedItem().equals("Bed")) {
            cdAvailableModels.getItems().clear();
            cdAvailableModels.getSelectionModel().clearSelection();
            ObservableList<String> models = FXCollections.observableArrayList("Sofa Bed", "Futon", "Air Mattress", "Baby Cot", "Medical Bed", "Camp Bed");
            Collections.sort(models);
            cdAvailableModels.getItems().addAll(models);
            cdAvailableModels.setVisible(true);
        }
        //continue setting the editable fields to the values of the request
        // set the item selected of the cdAvailableModels to the model of the request
        //cdAvailableModels.getSelectionModel().selectItem(fullFurnitureRequest.getModel());
        cdAvailableModels.setValue(fullFurnitureRequest.getModel());
        cdAvailableModels.setText(fullFurnitureRequest.getModel());
        cdAvailableModels.selectItem(fullFurnitureRequest.getModel());
        System.out.println("The model is: " + fullFurnitureRequest.getModel());
        System.out.println("The selection model is: " + cdAvailableModels.getSelectionModel().getSelectedItem());
        cdAssembly.getSelectionModel().selectItem(fullFurnitureRequest.getAssembly() ? "Yes" : "No");
        txtFldNotes.setText(fullFurnitureRequest.getNotes());
        cbEmployeesToAssign.getSelectionModel().selectItem(fullFurnitureRequest.getEmployee());
        cbLongName.getSelectionModel().selectItem(fullFurnitureRequest.getLocationName());
        cbChangeStatus.setVisible(true);
        cbChangeStatus.getSelectionModel().selectItem(fullFurnitureRequest.getRequestStatus());

        //set the submit button to say update
        btnSubmit.setText("Update");
        //remove the current onAction event
        btnSubmit.setOnAction(null);
        //add a new onAction event
        btnSubmit.setOnAction(e -> {
            //set the request fields to the new values
            fullFurnitureRequest.setType(cbAvailableFurniture.getValue());
            fullFurnitureRequest.setModel(cdAvailableModels.getValue());
            fullFurnitureRequest.setAssembly(stringToBoolean(cdAssembly.getValue()));
            fullFurnitureRequest.setNotes(txtFldNotes.getText());
            fullFurnitureRequest.setEmployee(cbEmployeesToAssign.getValue());
            fullFurnitureRequest.setLocationName(cbLongName.getValue());
            fullFurnitureRequest.setRequestStatus(cbChangeStatus.getValue());

            //update the request
            EFurnitureRequest.updateFurnitureRequest(fullFurnitureRequest);
            //send the fullFurnitureRequest to the info card controller
            currentInfoCardController.sendRequest(fullFurnitureRequest);
            //close the stage
            ((Stage) btnSubmit.getScene().getWindow()).close();
        });

        //set the reset and cancel buttons to not be visible
        btnReset.setVisible(false);
    }
}
