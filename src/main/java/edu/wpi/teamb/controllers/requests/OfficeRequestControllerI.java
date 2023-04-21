package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.EOfficeRequest;
import edu.wpi.teamb.entities.requests.IRequest;
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
import java.sql.Timestamp;

public class OfficeRequestControllerI implements IRequestController{

    @FXML
    private MFXButton btnSubmit;
    @FXML private MFXButton btnCancel;
    @FXML private MFXButton btnReset;
    @FXML private ImageView helpIcon;
    @FXML private MFXComboBox<String> cbSupplyItems;
    @FXML private MFXComboBox<String> cbSupplyType;
    @FXML private MFXTextField tbSupplyQuantities;
    @FXML private MFXTextField txtFldNotes;
    @FXML private MFXComboBox<String> cbEmployeesToAssign;
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
        btnCancel.setOnAction(e -> handleCancel());
        helpIcon.setOnMouseClicked(e -> handleHelp());
    }

    @Override
    public void initializeFields() throws SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cbLongName.setItems(longNames);

        //DROPDOWN INITIALIZATION
        ObservableList<String> employees = FXCollections.observableArrayList(EOfficeRequest.getUsernames());
        cbEmployeesToAssign.setItems(employees);

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplies = FXCollections.observableArrayList("Pencils", "Pens", "Paper", "Stapler", "Staples", "Tape", "Scissors", "Glue", "Markers", "Highlighters", "Post-It Notes", "Paper Clips", "Binder Clips", "Folders", "Envelopes", "Printer Paper");
        cbSupplyItems.setItems(supplies);

        //DROPDOWN INITIALIZATION
        ObservableList<String> supplyType = FXCollections.observableArrayList("Office Supplies", "Cleaning Supplies", "Other");
        cbSupplyType.setItems(supplyType);
    }

    @Override
    public void handleSubmit() {
        // Get the standard request fields
        EOfficeRequest.setEmployee(cbEmployeesToAssign.getValue());
        EOfficeRequest.setLocationName(cbLongName.getValue());
        EOfficeRequest.setRequestStatus(IRequest.RequestStatus.Pending);
        EOfficeRequest.setNotes(txtFldNotes.getText());

        // Get the office specific fields
        EOfficeRequest.setType(cbSupplyType.getValue());
        EOfficeRequest.setItem(cbSupplyItems.getValue());
        EOfficeRequest.setQuantity(Integer.parseInt(tbSupplyQuantities.getText()));

        if(EOfficeRequest.checkRequestFields() && EOfficeRequest.checkSpecialRequestFields()){
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
        submissionAlert();
    }

    @Override
    public void handleReset() {
        cbEmployeesToAssign.clear();
        cbEmployeesToAssign.replaceSelection("Employees Available");
        cbSupplyItems.clear();
        cbSupplyItems.replaceSelection("Available Supplies:");
        cbSupplyType.clear();
        cbSupplyType.replaceSelection("Supply Type:");
        tbSupplyQuantities.clear();
        tbSupplyQuantities.replaceSelection("Quantity:");
        txtFldNotes.clear();
        cbLongName.clear();
        cbLongName.replaceSelection("All Room Names:");
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
}
