package edu.wpi.teamb.controllers.mapeditor;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditNodeMenuController {
    @FXML
    MFXTextField tfNodeId;
    @FXML
    MFXTextField tfLongName;
    @FXML
    MFXTextField tfShortName;
    @FXML
    MFXButton btnSubmitNodeDetails;
    @FXML
    MFXFilterComboBox<String> cbNodeType;
    @FXML
    MFXTextField tfXCoord;
    @FXML
    MFXTextField tfYCoord;

    static FullNode currentNode = null;
    
    String oldLongName = "";
    String oldShortName ="";
    String oldNodeType = "";
    private static MapEditorController mapEditorController;

    public EditNodeMenuController() throws SQLException {
    }

    @FXML
    public void initialize() throws IOException {
        initializeFields();
        initButtons();
        tfNodeId.setEditable(false);
    }

    public void setMapEditorController(MapEditorController mapEditorController) {
        EditNodeMenuController.mapEditorController = mapEditorController;
    }


    public void initializeFields() {
        // Init combo box
        // initialize cbNodeType options
        ObservableList<String> listOfNodeTypes = FXCollections.observableArrayList(Repository.getRepository().getNodeTypesUniqueAlphabetical());
        cbNodeType.setItems(listOfNodeTypes);
        // Initialize the node data
        FullNode newFullNode = Repository.getRepository().getFullNode(currentNode.getNodeID());

        // Initialize the user data
        tfNodeId.setText(String.valueOf(newFullNode.getNodeID()));
        tfNodeId.setTooltip(new Tooltip("You cannot change the node ID"));
        tfLongName.setText(newFullNode.getLongName());
        tfLongName.setTooltip(new Tooltip("Enter the long name of the node"));
        tfShortName.setText(newFullNode.getShortName());
        tfShortName.setTooltip(new Tooltip("Enter the short name of the node"));
        cbNodeType.selectItem(newFullNode.getNodeType());
        cbNodeType.setTooltip(new Tooltip("Select a node type"));
        tfXCoord.setText(String.valueOf(currentNode.getxCoord()));
        tfXCoord.setTooltip(new Tooltip("Change the x coordinate of the node if needed"));
        tfYCoord.setText(String.valueOf(currentNode.getyCoord()));
        tfYCoord.setTooltip(new Tooltip("Change the y coordinate of the node if needed"));
        oldLongName = newFullNode.getLongName();
        oldShortName = newFullNode.getShortName();
        oldNodeType = newFullNode.getNodeType();
    }

    public void initButtons() {
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(tfNodeId.textProperty(),
                        tfLongName.textProperty(),
                        tfShortName.textProperty(),
                        tfXCoord.textProperty(),
                        tfYCoord.textProperty(),
                        cbNodeType.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (tfNodeId.getText().isEmpty() ||
                        tfLongName.getText().isEmpty() ||
                        tfShortName.getText().isEmpty() ||
                        tfXCoord.getText().isEmpty() ||
                        tfYCoord.getText().isEmpty() ||
                        cbNodeType.getValue() == null);
            }
        };
        btnSubmitNodeDetails.disableProperty().bind(bb);
        btnSubmitNodeDetails.setTooltip(new Tooltip("Submit the node details"));
        btnSubmitNodeDetails.setOnMouseClicked(event -> handleSubmitNodeDetails());
    }

    private void handleSubmitNodeDetails() {
        submitNode();
    }

    private void submitNode() {
        FullNode fullNode = null;

        // Delete old moves from the database
        ArrayList<Move> moves = new ArrayList<>(Repository.getRepository().getAllMoves());
        for (Move mv : moves) {
            if (mv.getNodeID() == currentNode.getNodeID()) {
                Repository.getRepository().deleteMove(mv);
            }
        }

        // Delete old location name from the database
        LocationName ln = new LocationName(oldLongName, oldShortName, oldNodeType);
        Repository.getRepository().deleteLocationName(ln);

        // Create new full node based on the user input
        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()), (int) Integer.parseInt(tfXCoord.getText()), (int) Integer.parseInt(tfYCoord.getText()), mapEditorController.currentFloor, currentNode.getBuilding(), tfLongName.getText(), tfShortName.getText(), cbNodeType.getSelectedItem());

        Repository.getRepository().deleteFullNode(fullNode); // Remove old node from the database
        Repository.getRepository().addFullNode(fullNode);  // Add new node to the database

        // Remove full node from the MapEditor's list and add the new one
        for (FullNode fn : MapEditorController.fullNodesList) {
            if (fn.getNodeID() == fullNode.getNodeID()) {
                MapEditorController.fullNodesList.remove(fn);
                MapEditorController.fullNodesList.add(fullNode);
                break;
            }
        }
        System.out.println("Editing a  node with nodeID: " + fullNode.getNodeID());
        submissionAlert("Node successfully edited! Refreshing the map.");
        mapEditorController.refreshMap();
        // Close the window
        Stage stage = (Stage) btnSubmitNodeDetails.getScene().getWindow();
        stage.close();
    }



    public void setCurrentNode(FullNode currentNode) {
        EditNodeMenuController.currentNode = currentNode;
    }
    void submissionAlert(String message) {
        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submission Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
