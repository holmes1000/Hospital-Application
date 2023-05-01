package edu.wpi.teamb.controllers.mapeditor;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddNodeMenuController {
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

    static Node currentNode = null;
    static String currentFloor = null;

    MapEditorController mapEditorController = new MapEditorController();

    public AddNodeMenuController() throws SQLException {
    }

    public static void setCurrentFloor(String currentFloor) {
        AddNodeMenuController.currentFloor = currentFloor;
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
        cbNodeType.setTooltip(new Tooltip("Select a node type"));
        // Initialize the node data
        //FullNode newFullNode = Repository.getRepository().getFullNode(currentNode.getNodeID());
        // Initialize the user data
        tfNodeId.setText(String.valueOf(currentNode.getNodeID()));
        tfNodeId.setTooltip(new Tooltip("Node ID cannot be changed"));
        tfXCoord.setText(String.valueOf(currentNode.getxCoord()));
        tfXCoord.setTooltip(new Tooltip("Change the x coordinate of the node if needed"));
        tfYCoord.setText(String.valueOf(currentNode.getyCoord()));
        tfYCoord.setTooltip(new Tooltip("Change the y coordinate of the node if needed"));
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
        btnSubmitNodeDetails.setOnMouseClicked(event -> handleSubmitNodeDetails());
        btnSubmitNodeDetails.setTooltip(new Tooltip("Click to submit the node details"));
    }

    private void handleSubmitNodeDetails() {
        submitNode();
        mapEditorController.refreshMap();
    }

    private void submitNode() {
        FullNode fullNode = null;
        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()), Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), currentFloor, "Full Node Building", tfLongName.getText(), tfShortName.getText(), cbNodeType.getSelectedItem());
        if (Objects.equals(cbNodeType.getText(), "ELEV") || Objects.equals(cbNodeType.getText(), "STAI")) {
            createMultiFloorNode(cbNodeType.getText());
        }
        else {
            Repository.getRepository().addFullNode(fullNode);
            Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
            MapEditorController.fullNodesList.add(fullNode);
            PathFinding.ASTAR.get_node_map().put(newNode.getNodeID(), newNode); // Add the node to the nodeMap
            System.out.println("Adding a new node with nodeID: " + newNode.getNodeID());
        }

        // Close the window
        Stage stage = (Stage) btnSubmitNodeDetails.getScene().getWindow();
        stage.close();
    }

    private void createMultiFloorNode(String nodeType) {
        FullNode fullNode = null;
        String longNodeType = "";
        if (nodeType.equals("ELEV"))
            longNodeType = "Elevator";
        else if (nodeType.equals("STAI"))
            longNodeType = "Stair";
        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()), Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), "1", "Full Node Building", longNodeType + " " + tfLongName.getText() + " 1", tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);
        createFullNode(fullNode);

        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()) + 5, Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), "2", "Full Node Building", longNodeType + " " + tfLongName.getText() + " 2", tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);
        createFullNode(fullNode);

        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()) + 10, Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), "3", "Full Node Building", longNodeType + " " + tfLongName.getText() + " 3", tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);
        createFullNode(fullNode);

        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()) +  15, Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), "L1", "Full Node Building", longNodeType + " " + tfLongName.getText() + " L1", tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);
        createFullNode(fullNode);

        fullNode = new FullNode(Integer.parseInt(tfNodeId.getText()) + 20, Integer.parseInt(tfXCoord.getText()), Integer.parseInt(tfYCoord.getText()), "L2", "Full Node Building", longNodeType + " " + tfLongName.getText() + " L2", tfShortName.getText(), cbNodeType.getSelectedItem());
        Repository.getRepository().addFullNode(fullNode);
        createFullNode(fullNode);
    }

    private void createFullNode(FullNode fullNode) {
        Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
        MapEditorController.fullNodesList.add(fullNode);
        PathFinding.ASTAR.get_node_map().put(newNode.getNodeID(), newNode); // Add the node to the nodeMap
    }


    public static void setCurrentNode(Node currentNode) {
        AddNodeMenuController.currentNode = currentNode;
    }
}
