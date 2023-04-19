package edu.wpi.teamb.controllers.requests;


import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.FlowerRequestE;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;

public class FlowerRequestController {

    //Create a fxml flower request controller

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
    private MFXComboBox<String> cbAvailableFlowers;
    @FXML
    private MFXComboBox<String> cdAvailableColor;
    @FXML
    private MFXComboBox<String> cdAvailableType;
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

    private FlowerRequestE flowerRequestE;

    public FlowerRequestController() {
        this.flowerRequestE = new FlowerRequestE();
    }

    @FXML
    public void initialize() throws IOException, SQLException {

        initializeFields();
        clickHelp();

    }

    @FXML
    public void clickSubmit() {
        btnSubmit.setOnMouseClicked(event -> {

            //Get all fields from request
            String orderfrom = cbOrderLocation.getSelectedItem();
            String flower = cbAvailableFlowers.getSelectedItem();
            String color = cdAvailableColor.getSelectedItem();
            String deliverytype = cdAvailableType.getSelectedItem();
            String message = txtFldMessage.getText();
            String employee = cbEmployeesToAssign.getSelectedItem();
            String floor = cbFloorSelect.getSelectedItem();
            String longName = cbLongName.getSelectedItem();
            String roomnumber = roomTextBox.getText();
            String notes = txtFldNotes.getText();
            String requeststatus =("Pending");
            String flowerrequesttype = ("Flower");

            //Check for required fields before allowing submittion
            if((orderfrom != null) && (flower != null) && (color != null) && (deliverytype != null) && (floor != null) && (roomnumber != null) && (longName != null)){

                //Set the gathered fields into a string array
                String[] output = {employee, floor, roomnumber, requeststatus, flowerrequesttype, orderfrom, flower, color, deliverytype, message, longName, notes};
                flowerRequestE.submitRequest(output);
                clickResetForm();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            }else{

                //If the required fields are not filled, bring up pop-over indicating such
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
        });
    }

    @FXML
    public void clickCancel() {
        btnCancel.setOnMouseClicked(event -> {
            Navigation.navigate(Screen.HOME);
        });
    }

    @FXML
    public void clickResetForm() {
        btnReset.setOnMouseClicked(event -> {
            cbAvailableFlowers.getSelectionModel().clearSelection();
            cdAvailableColor.getSelectionModel().clearSelection();
            cdAvailableType.getSelectionModel().clearSelection();
            txtFldMessage.clear();
            txtFldNotes.clear();
            cbEmployeesToAssign.getSelectionModel().clearSelection();
            cbFloorSelect.getSelectionModel().clearSelection();
            roomTextBox.clear();
            cbLongName.clear();
            cbLongName.replaceSelection("All Room Names: ");
            cbOrderLocation.getSelectionModel().clearSelection();
        });
    }

    public void initializeFields() throws SQLException {

        //Initialize the list of locations to direct request to via dropdown
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cbLongName.setItems(longNames);

        //Set types of flowers
        ObservableList<String> flowers = FXCollections.observableArrayList("Rose", "Tulip", "Daisy", "Lily", "Sunflower");
        cbAvailableFlowers.setItems(flowers);

        //Set colors of flowers
        ObservableList<String> colors = FXCollections.observableArrayList("Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Pink", "White", "Black", "Brown", "Other");
        cdAvailableColor.setItems(colors);


        //Set delivery types
        ObservableList<String> deliveryType = FXCollections.observableArrayList("Bouquet", "Single Flower", "Vase", "Other");
        cdAvailableType.setItems(deliveryType);
    //todo: fix locations

        //Set list of order locations
        ObservableList<String> orderLocation = FXCollections.observableArrayList("Main Lobby", "ER", "ICU", "Other");
        cbOrderLocation.setItems(orderLocation);

        //Set list of floors
        ObservableList<String> floors =
                FXCollections.observableArrayList("Lower Floor 1", "Lower Floor 2", "Floor 1", "Floor 2", "Floor 3");
        cbFloorSelect.setItems(floors);

        //Set list of employees
        ObservableList<String> employees =
                FXCollections.observableArrayList();
        employees.addAll(flowerRequestE.getUsernames());
        cbEmployeesToAssign.setItems(employees);
    }

    public void clickHelp() {
        //TODO -> Must fix the help
        helpIcon.setOnMouseClicked(
                event -> {
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
                });
    }


}
