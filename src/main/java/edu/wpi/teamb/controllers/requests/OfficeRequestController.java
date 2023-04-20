package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.EOfficeRequest;
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

public class OfficeRequestController {

    @FXML
    private MFXButton btnSubmit;
    @FXML private MFXButton btnCancel;
    @FXML private MFXButton btnReset;
    @FXML private MFXTextField roomTextBox;
    @FXML private ImageView helpIcon;
    @FXML private MFXComboBox<String> cbSupplyItems;
    @FXML private MFXComboBox<String> cbSupplyType;
    @FXML private MFXTextField tbSupplyQuantities;
    @FXML private MFXTextField txtFldNotes;
    @FXML private MFXComboBox<String> cbEmployeesToAssign;
    @FXML private MFXComboBox<String> cbFloorSelect; // Floor
    @FXML private MFXFilterComboBox<String> cbLongName;

    private EOfficeRequest EOfficeRequest;

    public OfficeRequestController(){
        this.EOfficeRequest = new EOfficeRequest();
    }

    @FXML
    public void initialize() throws SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cbLongName.setItems(longNames);
        initializeFields();
        hoverHelp();
    }

    @FXML
    public void clickSubmit() {
        btnSubmit.setOnMouseClicked(event -> {
            String item = cbSupplyItems.getSelectedItem();
            String type = cbSupplyType.getSelectedItem();
            String quantity = tbSupplyQuantities.getText();
            String notes = txtFldNotes.getText();
            String floor = cbFloorSelect.getSelectedItem();
            String longName = cbLongName.getSelectedItem();
            String roomnumber = roomTextBox.getText();
            String employee = cbEmployeesToAssign.getSelectedItem();
            String location = cbLongName.getSelectedItem();
            String requeststatus = "Pending";
            String officetype = "Office";
            if((item != null) && (type != null) && (quantity != null) && (notes != null) && (floor != null) && (longName != null) && (roomnumber != null) && (employee != null) && (requeststatus != null) && (location != null)){
                String[] output = {employee, floor, roomnumber, requeststatus, officetype, item, type, quantity, notes, location};
                EOfficeRequest.submitRequest(output);
                clickResetForm();
                Navigation.navigate(Screen.CREATE_NEW_REQUEST);
            } else {
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
        });
    }

    public void clickCancel() {
        btnCancel.setOnMouseClicked(event -> {
            Navigation.navigate(Screen.HOME);
        });
    }

    public void clickResetForm() {
        btnReset.setOnMouseClicked(event -> {
            cbFloorSelect.clear();
            cbFloorSelect.replaceSelection("Floor");
            cbEmployeesToAssign.clear();
            cbEmployeesToAssign.replaceSelection("Employees Available");
            roomTextBox.clear();
            roomTextBox.replaceSelection("Room:");
            cbSupplyItems.clear();
            cbSupplyItems.replaceSelection("Available Supplies:");
            cbSupplyType.clear();
            cbSupplyType.replaceSelection("Supply Type:");
            tbSupplyQuantities.clear();
            tbSupplyQuantities.replaceSelection("Quantity:");
            txtFldNotes.clear();
            cbLongName.clear();
            cbLongName.replaceSelection("All Room Names:");
        });
    }

    public void hoverHelp() throws SQLException {
        helpIcon.setOnMouseClicked(
                event -> {
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
                });
    }

        private void initializeFields() throws SQLException{
            //DROPDOWN INITIALIZATION
//            ObservableList<String> locations = FXCollections.observableArrayList("Tower", "Connors Center", "Shapiro Center");
//            cbOrderLocation.setItems(locations);

            //DROPDOWN INITIALIZATION
            ObservableList<String> employees = FXCollections.observableArrayList(EOfficeRequest.getUsernames());
            cbEmployeesToAssign.setItems(employees);

            //DROPDOWN INITIALIZATION
            ObservableList<String> floors = FXCollections.observableArrayList("Floor 1", "Floor 2", "Floor 3", "Floor 4");
            cbFloorSelect.setItems(floors);

            //DROPDOWN INITIALIZATION
            ObservableList<String> supplies = FXCollections.observableArrayList("Pencils", "Pens", "Paper", "Stapler", "Staples", "Tape", "Scissors", "Glue", "Markers", "Highlighters", "Post-It Notes", "Paper Clips", "Binder Clips", "Folders", "Envelopes", "Printer Paper");
            cbSupplyItems.setItems(supplies);

            //DROPDOWN INITIALIZATION
            ObservableList<String> supplyType = FXCollections.observableArrayList("Office Supplies", "Cleaning Supplies", "Other");
            cbSupplyType.setItems(supplyType);
        }
        @FXML
        public void clickExit() {
            System.exit(0);
        }
}
