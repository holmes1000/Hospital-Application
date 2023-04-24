package edu.wpi.teamb.controllers.mapeditor;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

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
    MFXComboBox<String> cbNodeType;
    @FXML
    MFXTextField tfXCoord;
    @FXML
    MFXTextField tfYCoord;

    static Node currentNode = null;

    MapEditorController mapEditorController = new MapEditorController();

    public EditNodeMenuController() throws SQLException {
    }

    @FXML
    public void initialize() throws IOException {
        initializeFields();
        initButtons();
        tfNodeId.setEditable(false);
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
        tfLongName.setText(newFullNode.getLongName());
        tfShortName.setText(newFullNode.getShortName());
        cbNodeType.selectItem(newFullNode.getNodeType());
        tfXCoord.setText(String.valueOf(newFullNode.getxCoord()));
        tfYCoord.setText(String.valueOf(newFullNode.getyCoord()));
    }

    public void initButtons() {
        btnSubmitNodeDetails.setOnMouseClicked(event -> handleSubmitNodeDetails());
    }

    private void handleSubmitNodeDetails() {
        submitNode();
    }

    private void submitNode() {
        FullNode fullNode = null;


        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()), (int) mapEditorController.fullNodeX, (int) mapEditorController.fullNodeY, mapEditorController.currentFloor, "Full Node Building", tfLongName.getText(), tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);

        Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
        mapEditorController.nodeList.add(newNode); // Add the node to the nodeList

        // Add node to the database
        //Repository.getRepository().addNode(newNode);

        System.out.println("Adding a new node with nodeID: " + newNode.getNodeID());
        // Refresh the map
        //refreshMap();


        // Close the window
        Stage stage = (Stage) btnSubmitNodeDetails.getScene().getWindow();
        stage.close();
    }



    public static void setCurrentNode(Node currentNode) {
        EditNodeMenuController.currentNode = currentNode;
    }
}
