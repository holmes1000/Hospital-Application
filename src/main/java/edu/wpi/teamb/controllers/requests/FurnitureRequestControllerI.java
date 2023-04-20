package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.EFurnitureRequest;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;

public class FurnitureRequestControllerI implements IRequestController{

    @FXML
    private MFXButton btnSubmit;
    @FXML
    private MFXButton btnCancel;
    @FXML
    private MFXButton btnReset;
    @FXML
    private MFXTextField roomTextBox;
    @FXML
    private ImageView helpIcon;
    @FXML
    private MFXComboBox<String> cbAvailableFurniture;
    @FXML
    private MFXComboBox<String> cdAvailableModels;
    @FXML
    private MFXComboBox<String> cdAssembly;
    @FXML
    private MFXTextField txtFldNotes;
    @FXML
    private MFXTextField txtFldMessage;
    @FXML
    private MFXComboBox<String> cbOrderLocation;
    @FXML
    private MFXComboBox<String> cbEmployeesToAssign;
    @FXML
    private MFXComboBox<String> cbFloorSelect; // Floor

    @FXML private MFXFilterComboBox<String> cbLongName;

    private edu.wpi.teamb.entities.requests.EFurnitureRequest EFurnitureRequest;

    public FurnitureRequestControllerI() {
        this.EFurnitureRequest = new EFurnitureRequest();
    }

    @FXML
    public void initialize() throws IOException, SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cbLongName.setItems(longNames);
        initializeFields();
        initBtns();
    }

    @Override
    public void initBtns() {
        btnSubmit.setOnAction(e -> handleSubmit());
        btnReset.setOnAction(e -> handleReset());
        btnCancel.setOnAction(e -> handleCancel());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        //Set list of furniture
        ObservableList<String> furniture = FXCollections.observableArrayList("Chair", "Couch", "Table", "Desk", "Bed");
        cbAvailableFurniture.setItems(furniture);

        //Set list of models
        ObservableList<String> models = FXCollections.observableArrayList("Huge", "Big", "Medium", "Small", "Tiny");
        cdAvailableModels.setItems(models);

        //Set list of assembly options
        ObservableList<String> assembly = FXCollections.observableArrayList("No", "Yes");
        cdAssembly.setItems(assembly);
        //todo: fix locations

        //Set list of locations to order to
        ObservableList<String> orderLocation = FXCollections.observableArrayList("Main Lobby", "ER", "ICU", "Other");
        cbOrderLocation.setItems(orderLocation);


        //Set list of floors
        ObservableList<String> floors =
                FXCollections.observableArrayList("Lower Floor 1", "Lower Floor 2", "Floor 1", "Floor 2", "Floor 3");
        cbFloorSelect.setItems(floors);

        //Set list of employees
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(EFurnitureRequest.getUsernames());
        cbEmployeesToAssign.setItems(employees);
    }

    @Override
    public void handleSubmit() {
        String orderfrom = cbOrderLocation.getSelectedItem();
        String furniture = cbAvailableFurniture.getSelectedItem();
        String model = cdAvailableModels.getSelectedItem();
        String assembly = cdAssembly.getSelectedItem();
        String message = txtFldMessage.getText();
        String employee = cbEmployeesToAssign.getSelectedItem();
        String floor = cbFloorSelect.getSelectedItem();
        String longName = cbLongName.getSelectedItem();
        String roomnumber = roomTextBox.getText();
        String notes = txtFldNotes.getText();
        String requeststatus =("Pending");
        String furniturerequesttype = ("Furniture");
        if((orderfrom != null) && (furniture != null) && (model != null) && (assembly != null) && (floor != null) && (roomnumber != null) && (longName != null)){
            String[] output = {employee, floor, roomnumber, requeststatus, furniturerequesttype, orderfrom, furniture, model, message, longName, notes};
            EFurnitureRequest.submitRequest(output);
            handleReset();
            Navigation.navigate(Screen.CREATE_NEW_REQUEST);
        }else{
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/NotAllFieldsCompleteError.fxml"));
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
        submissionAlert();
    }

    @Override
    public void handleReset() {
        cbAvailableFurniture.getSelectionModel().clearSelection();
        cdAvailableModels.getSelectionModel().clearSelection();
        cdAssembly.getSelectionModel().clearSelection();
        txtFldMessage.clear();
        txtFldNotes.clear();
        cbEmployeesToAssign.getSelectionModel().clearSelection();
        cbFloorSelect.getSelectionModel().clearSelection();
        roomTextBox.clear();
        cbLongName.clear();
        cbLongName.replaceSelection("All Room Names: ");
        cbOrderLocation.getSelectionModel().clearSelection();
    }

    @Override
    public void handleHelp() {
        final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/FurnitureRequestHelpPopOver.fxml"));
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
}
